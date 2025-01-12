package orderlistservice.adapters.outbound.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import orderlistservice.application.ports.outbound.PriceMonitoringPort;
import orderlistservice.domain.model.PriceResult;
import orderlistservice.pricecheck.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class PriceCheckGrpcAdapter implements PriceMonitoringPort {

    private final PriceCheckServiceGrpc.PriceCheckServiceBlockingStub stub;

    // Injiziere die Konfigurationswerte
    @ConfigProperty(name = "pricecheck.host")
    String host;

    @ConfigProperty(name = "pricecheck.port")
    int port;

    public PriceCheckGrpcAdapter() {
        // Erstelle den gRPC-Channel
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext() // Für lokale Tests ohne TLS
                .build();
        this.stub = PriceCheckServiceGrpc.newBlockingStub(channel);
    }

    @Override
    public List<PriceResult> checkPrices(List<Integer> itemIds) {
        // Baue die Anfrage

        PriceCheckRequest.Builder requestBuilder = PriceCheckRequest.newBuilder();
        for (Integer id : itemIds) {
            requestBuilder .addItemIds(id);
        }

        PriceCheckRequest request = requestBuilder.build();

        // gRPC-Aufruf an den externen Service
        PriceCheckReply reply = stub.checkPrices(request);

        // Mapping der Protobuf-Antwort (`PriceResult`) auf das Domänenmodell (`PriceResult`)
        return reply.getResultsList().stream()
                .map(protoPriceResult -> new PriceResult(
                        protoPriceResult.getItemId(),
                        protoPriceResult.getStatus(),
                        protoPriceResult.getMessage(),
                        protoPriceResult.getLogId(),
                        BigDecimal.valueOf(protoPriceResult.getPrice()), // Konvertiere double zu BigDecimal
                        protoPriceResult.getAvailability()
                ))
                .collect(Collectors.toList());
    }
}
