package adapters.outbound.grpc;

import application.port.ObjectManagementPort;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import objectmanagement.ObjectManagementServiceGrpc;
import objectmanagement.StorageObjectIdsRequest;
import objectmanagement.StorageObjectsReply;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
public class ObjectManagementGrpcClient implements ObjectManagementPort {

    private final ObjectManagementServiceGrpc.ObjectManagementServiceBlockingStub stub;

    // Injektion der Konfigurationswerte
    @ConfigProperty(name = "objectmanagement.host")
    String host;

    @ConfigProperty(name = "objectmanagement.port")
    int port;

    public ObjectManagementGrpcClient() {
        // Erstelle den gRPC-Channel mit dynamischen Host/Port
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext() // Für lokale Tests ohne TLS
                .build();
        this.stub = ObjectManagementServiceGrpc.newBlockingStub(channel);
    }

    @Override
    public Map<Integer, String> getReorderUrlsByIds(List<Integer> itemIds) {
        StorageObjectIdsRequest request = StorageObjectIdsRequest.newBuilder()
                .addAllIds(itemIds)
                .build();

        StorageObjectsReply reply = stub.getStorageObjectsByIds(request);

        return reply.getStorageObjectsList().stream()
                .collect(Collectors.toMap(
                        object -> object.getId(),
                        object -> object.getReorderUrl()
                ));
    }
}
