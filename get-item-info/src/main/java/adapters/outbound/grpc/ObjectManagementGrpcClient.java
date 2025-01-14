package adapters.outbound.grpc;

import application.port.ObjectManagementPort;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import objects.ObjectServiceGrpc;
import objects.StorageObjectIdsRequest;
import objects.StorageObjectsReply;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
public class ObjectManagementGrpcClient implements ObjectManagementPort {

    private static final Logger LOGGER = LoggerFactory.getLogger(ObjectManagementGrpcClient.class);

    private ObjectServiceGrpc.ObjectServiceBlockingStub stub;

    @ConfigProperty(name = "objectmanagement.host")
    String host;

    @ConfigProperty(name = "objectmanagement.port")
    int port;

    @PostConstruct
    void init() {
        LOGGER.info("Initialisiere gRPC-Client für ObjectService (Host: {}, Port: {})...", host, port);
        try {
            ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                    .usePlaintext()
                    .build();
            this.stub = ObjectServiceGrpc.newBlockingStub(channel);
            LOGGER.info("gRPC-Client erfolgreich initialisiert.");
        } catch (Exception e) {
            LOGGER.error("Fehler beim Initialisieren des gRPC-Channels: {}", e.getMessage(), e);
            throw new IllegalStateException("gRPC-Client konnte nicht initialisiert werden.", e);
        }
    }

    @Override
    public Map<Integer, String> getReorderUrlsByIds(List<Integer> itemIds) {
        try {
            LOGGER.debug("Sende gRPC-Anfrage mit Item-IDs: {}", itemIds);

            var request = StorageObjectIdsRequest.newBuilder()
                    .addAllIds(itemIds)
                    .build();

            var reply = stub.getStorageObjectsByIds(request);

            return reply.getStorageObjectsList().stream()
                    .collect(Collectors.toMap(
                            object -> object.getId(),
                            object -> object.getReorderUrl()
                    ));
        } catch (Exception e) {
            LOGGER.error("Fehler beim Abrufen der Reorder-URLs über gRPC: {}", e.getMessage(), e);
            throw new RuntimeException("Fehler beim Abrufen der Reorder-URLs.", e);
        }
    }
}