package orderlistservice.adapters.outbound.grpc;

import jakarta.annotation.PostConstruct;
import orderlistservice.objects.*;
import orderlistservice.application.ports.outbound.ObjectManagementPort;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import orderlistservice.domain.model.ItemDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ObjectManagementGrpcAdapter implements ObjectManagementPort {
    private static final Logger LOGGER = LoggerFactory.getLogger(ObjectManagementGrpcAdapter.class);

    private ObjectServiceGrpc.ObjectServiceBlockingStub stub;

    // Konfigurationswerte für Host und Port
    @ConfigProperty(name = "objectmanagement.host")
    String host;

    @ConfigProperty(name = "objectmanagement.port")
    int port;

    @PostConstruct
    void init() {
        try {
            LOGGER.info("Initialisiere gRPC-Client für ObjectService (Host: {}, Port: {})...", host, port);

            ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                    .usePlaintext() // Für lokale Tests ohne TLS
                    .build();

            this.stub = ObjectServiceGrpc.newBlockingStub(channel);

            LOGGER.info("gRPC-Client erfolgreich initialisiert.");
        } catch (Exception e) {
            LOGGER.error("Fehler beim Initialisieren des gRPC-Clients für ObjectService: {}", e.getMessage(), e);
            throw new IllegalStateException("gRPC-Client konnte nicht initialisiert werden.", e);
        }
    }

    @Override
    public List<ItemDetails> getItemDetails(List<Integer> itemIds) {
        try {
            LOGGER.debug("Sende gRPC-Anfrage an ObjectService mit Item-IDs: {}", itemIds);

            // Anfrage erstellen
            StorageObjectIdsRequest request = StorageObjectIdsRequest.newBuilder()
                    .addAllIds(itemIds)
                    .build();

            // Antwort vom gRPC-Service abrufen
            StorageObjectsReply reply = stub.getStorageObjectsByIds(request);

            // Antwort in ItemDetails-Objekte mappen
            List<ItemDetails> itemDetailsList = reply.getStorageObjectsList().stream()
                    .map(storageObject -> new ItemDetails(
                            storageObject.getId(),
                            storageObject.getName(),
                            storageObject.getDescription(),
                            storageObject.getReorderUrl(),
                            storageObject.getQuantity()
                    ))
                    .collect(Collectors.toList());

            LOGGER.info("Erfolgreich {} ItemDetails abgerufen.", itemDetailsList.size());
            return itemDetailsList;
        } catch (Exception e) {
            LOGGER.error("Fehler beim Abrufen von ItemDetails von ObjectService: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }
}
