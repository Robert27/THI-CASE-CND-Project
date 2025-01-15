package orderlistservice.adapters.inbound.grpc;

import io.grpc.stub.StreamObserver;
import io.quarkus.grpc.GrpcService;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import orderlistservice.domain.OrderService;
import orderlistservice.order.GenerateOrderListRequest;
import orderlistservice.order.GenerateOrderListReply;
import orderlistservice.order.OrderListServiceGrpc;
import java.util.concurrent.ExecutorService;

@GrpcService
@Singleton
public class OrderListGrpcService extends OrderListServiceGrpc.OrderListServiceImplBase {


    @Inject
    OrderService orderUseCase;

    @Inject
    @Named("myExecutorService")
    ExecutorService executor;  // aus der Producer-Methode

    @Override
    public void generateOrderList(GenerateOrderListRequest request,
                                  StreamObserver<GenerateOrderListReply> responseObserver) {
        // Auslagern in einen Worker-Thread
        executor.submit(() -> {
            try {
                int statusCode = orderUseCase.generateOrderList(
                        request.getUserId(),
                        request.getObjectIdsList(),
                        request.getDate()
                );

                GenerateOrderListReply.Builder replyBuilder = GenerateOrderListReply.newBuilder();
                replyBuilder.setSuccess(statusCode == 200);
                GenerateOrderListReply reply = replyBuilder.build();

                responseObserver.onNext(reply);
                responseObserver.onCompleted();

            } catch (Exception e) {
                // Im Fehlerfall
                responseObserver.onError(e);
            }
        });
    }
}