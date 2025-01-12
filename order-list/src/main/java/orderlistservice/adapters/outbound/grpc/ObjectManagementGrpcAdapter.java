package orderlistservice.adapters.outbound.grpc;

import orderlistservice.objectmanagement.*;
import orderlistservice.application.ports.outbound.ObjectManagementPort;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import jakarta.enterprise.context.ApplicationScoped;
import orderlistservice.pricecheck.PriceCheckRequest;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import orderlistservice.domain.model.ItemDetails;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ObjectManagementGrpcAdapter implements ObjectManagementPort {

    private final ObjectManagementServiceGrpc.ObjectManagementServiceBlockingStub stub;

    // Injiziere die Konfigurationswerte
    @ConfigProperty(name = "objectmanagement.host")
    String host;

    @ConfigProperty(name = "objectmanagement.port")
    int port;


    public ObjectManagementGrpcAdapter() {
        // Erstelle den gRPC-Channel
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext() // Für lokale Tests ohne TLS
                .build();
        this.stub = ObjectManagementServiceGrpc.newBlockingStub(channel);
    }

    @Override
    public List<ItemDetails> getItemDetails(List<Integer> itemIds) {
        // Baue die Anfrage

        StorageObjectIdsRequest.Builder requestBuilder = StorageObjectIdsRequest.newBuilder();
        for (Integer id : itemIds) {
            requestBuilder.addIds(id);
        }

        StorageObjectIdsRequest request = requestBuilder.build();

        // gRPC-Aufruf an den externen Service
        StorageObjectsReply reply = stub.getStorageObjectsByIds(request);

        // Mapping der Protobuf-Antwort (`StorageObject`) auf das Domänenmodell (`ItemDetails`)
        return reply.getStorageObjectsList().stream()
                .map(storageObject -> new ItemDetails(
                        storageObject.getId(),
                        storageObject.getName(),
                        storageObject.getDescription(),
                        storageObject.getReorderUrl(),
                        storageObject.getQuantity()
                ))
                .collect(Collectors.toList());
    }
}
