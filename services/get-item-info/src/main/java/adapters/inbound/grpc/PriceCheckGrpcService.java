package adapters.inbound.grpc;

import domain.ItemPriceService;
import domain.model.BulkCheckResult;
import io.grpc.stub.StreamObserver;
import io.quarkus.grpc.GrpcService;
import io.smallrye.common.annotation.Blocking;
import jakarta.inject.Singleton;
import pricecheck.PriceCheckRequest;
import pricecheck.PriceCheckReply;
import pricecheck.PriceCheckServiceGrpc;
import jakarta.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@GrpcService
@Singleton
public class PriceCheckGrpcService extends PriceCheckServiceGrpc.PriceCheckServiceImplBase {

    private static final Logger LOG = LoggerFactory.getLogger(PriceCheckGrpcService.class);

    @Inject
    ItemPriceService itemPriceService;

    @Override
    @Blocking
    public void checkPrices(PriceCheckRequest request, StreamObserver<PriceCheckReply> responseObserver) {
        // Log-Einstieg
        LOG.info("GRPC checkPrices aufgerufen mit Request: {}", request);

        try {
            List<Integer> itemIds = request.getItemIdsList();
            LOG.debug("Item IDs empfangen: {}", itemIds);

            // Aufruf des Domänen-Services
            List<BulkCheckResult> results = itemPriceService.bulkCheckItems(itemIds);
            LOG.debug("Ergebnisse vom ItemPriceService: {}", results);

            // Zusammenbauen der Antwort
            PriceCheckReply.Builder replyBuilder = PriceCheckReply.newBuilder();
            for (BulkCheckResult result : results) {
                replyBuilder.addResults(
                        pricecheck.PriceResult.newBuilder()
                                .setItemId(result.getItemId())
                                .setStatus(result.getStatus())
                                .setMessage(result.getMessage() != null ? result.getMessage() : "")
                                .setLogId(result.getLogId() != null ? result.getLogId() : 0)
                                .setPrice(result.getPrice())
                                .setAvailability(result.getAvailability())
                                .build()
                );
            }

            // Senden der Antwort
            PriceCheckReply reply = replyBuilder.build();
            LOG.debug("Antwort wird gesendet: {}", reply);
            responseObserver.onNext(reply);
            responseObserver.onCompleted();
            LOG.info("GRPC checkPrices erfolgreich abgeschlossen.");

        } catch (Exception e) {
            LOG.error("Fehler in checkPrices: {}", e.getMessage(), e);
            responseObserver.onError(
                    io.grpc.Status.INTERNAL
                            .withDescription("Internal server error: " + e.getMessage())
                            .withCause(e)
                            .asRuntimeException()
            );
        }
    }
}
