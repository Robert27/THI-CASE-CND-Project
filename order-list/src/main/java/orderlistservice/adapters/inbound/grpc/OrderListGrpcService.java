package orderlistservice.adapters.inbound.grpc;

import io.grpc.stub.StreamObserver;
import io.quarkus.grpc.GrpcService;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import orderlistservice.domain.OrderService;
import orderlistservice.order.GenerateOrderListRequest;
import orderlistservice.order.GenerateOrderListReply;
import orderlistservice.order.OrderListServiceGrpc;

import jakarta.enterprise.context.ApplicationScoped;

@GrpcService
@Singleton
public class OrderListGrpcService extends OrderListServiceGrpc.OrderListServiceImplBase {

    @Inject
    OrderService orderUseCase;

    @Override
    public void generateOrderList(GenerateOrderListRequest request,
                                  StreamObserver<GenerateOrderListReply> responseObserver) {
        int statusCode = orderUseCase.generateOrderList(request.getUserId(), request.getObjectIdsList(), request.getDate());

        GenerateOrderListReply.Builder replyBuilder = GenerateOrderListReply.newBuilder();
        replyBuilder.setSuccess(statusCode == 200);
        GenerateOrderListReply reply = replyBuilder.build();
        // Sende die Antwort an den Client
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}
