package orderlistservice.adapters.outbound.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import orderlistservice.application.ports.outbound.PriceMonitoringPort;
import orderlistservice.domain.model.PriceResult;
import orderlistservice.pricecheck.PriceCheckReply;
import orderlistservice.pricecheck.PriceCheckRequest;
import orderlistservice.pricecheck.PriceCheckServiceGrpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;


@ApplicationScoped
public class PriceCheckGrpcAdapter implements PriceMonitoringPort {

    private static final Logger LOGGER = LoggerFactory.getLogger(PriceCheckGrpcAdapter.class);

    private PriceCheckServiceGrpc.PriceCheckServiceBlockingStub stub;

    @ConfigProperty(name = "pricecheck.host")
    String host;

    @ConfigProperty(name = "pricecheck.port")
    int port;

    @PostConstruct
    void init() {
        LOGGER.info("Initialisiere gRPC-Adapter für PriceCheckService (Host: {}, Port: {})...", host, port);
        try {
            ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                    .usePlaintext() // Nur für lokale Tests ohne TLS
                    .build();
            this.stub = PriceCheckServiceGrpc.newBlockingStub(channel);
            LOGGER.info("gRPC-Adapter erfolgreich initialisiert.");
        } catch (Exception e) {
            LOGGER.error("Fehler beim Initialisieren des gRPC-Channels: {}", e.getMessage(), e);
            throw new IllegalStateException("gRPC-Adapter konnte nicht initialisiert werden.", e);
        }
    }

    @Override
    public List<PriceResult> checkPrices(List<Integer> itemIds) {
        try {
            LOGGER.debug("Sende gRPC-Anfrage mit Item-IDs: {}", itemIds);

            // Baue die Anfrage
            PriceCheckRequest request = PriceCheckRequest.newBuilder()
                    .addAllItemIds(itemIds)
                    .build();

            // gRPC-Aufruf an den externen Service
            PriceCheckReply reply = stub.checkPrices(request);

            // Mapping der Antwort auf Domänenmodell
            List<PriceResult> results = reply.getResultsList().stream()
                    .map(protoPriceResult -> new PriceResult(
                            protoPriceResult.getItemId(),
                            protoPriceResult.getStatus(),
                            protoPriceResult.getMessage(),
                            protoPriceResult.getLogId(),
                            BigDecimal.valueOf(protoPriceResult.getPrice()), // Konvertiere double zu BigDecimal
                            protoPriceResult.getAvailability()
                    ))
                    .collect(Collectors.toList());

            LOGGER.info("Erfolgreich {} Preise überprüft.", results.size());
            return results;
        } catch (Exception e) {
            LOGGER.error("Fehler beim Abrufen der Preise über gRPC: {}", e.getMessage(), e);
            throw new RuntimeException("Fehler beim Abrufen der Preise.", e);
        }
    }
}