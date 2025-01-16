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

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@GrpcService
@Singleton
public class PriceCheckGrpcService extends PriceCheckServiceGrpc.PriceCheckServiceImplBase {

    @Inject
    ItemPriceService itemPriceService;

    @Override
    @Blocking
    public void checkPrices(PriceCheckRequest request, StreamObserver<PriceCheckReply> responseObserver) {
        try {
            System.out.println("Received request: " + request);
            List<Integer> itemIds = request.getItemIdsList();
            System.out.println("Item IDs: " + itemIds);

            List<BulkCheckResult> results = itemPriceService.bulkCheckItems(itemIds);
            System.out.println("Results: " + results);

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

            responseObserver.onNext(replyBuilder.build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            System.err.println("Error processing request: " + e.getMessage());
            e.printStackTrace();
            responseObserver.onError(io.grpc.Status.INTERNAL
                    .withDescription("Internal server error: " + e.getMessage())
                    .withCause(e)
                    .asRuntimeException());
        }
    }
}
