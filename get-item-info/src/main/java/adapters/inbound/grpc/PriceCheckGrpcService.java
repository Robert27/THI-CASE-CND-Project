package adapters.inbound.grpc;

import domain.ItemPriceService;
import domain.model.BulkCheckResult;
import io.grpc.stub.StreamObserver;
import io.quarkus.grpc.GrpcService;
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
    public void checkPrices(PriceCheckRequest request, StreamObserver<PriceCheckReply> responseObserver) {
        List<Integer> itemIds = request.getItemIdsList();

        List<BulkCheckResult> results = itemPriceService.bulkCheckItems(itemIds);

        PriceCheckReply.Builder replyBuilder = PriceCheckReply.newBuilder();
        for (BulkCheckResult result : results) {
            replyBuilder.addResults(
                    pricecheck.PriceResult.newBuilder()
                            .setItemId(result.getItemId())
                            .setStatus(result.getStatus())
                            .setMessage(result.getMessage() != null ? result.getMessage() : "")
                            .setLogId(result.getLogId())
                            .setPrice(result.getPrice())
                            .setAvailability(result.getAvailability())
                            .build()
            );
        }

        responseObserver.onNext(replyBuilder.build());
        responseObserver.onCompleted();
    }
}
