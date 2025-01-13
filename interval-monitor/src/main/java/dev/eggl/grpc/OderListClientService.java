package dev.eggl.grpc;

import dev.eggl.orderlist.OrderListService;
import dev.eggl.orderlist.GenerateOrderListRequest;
import dev.eggl.orderlist.GenerateOrderListReply;
import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class OderListClientService {

    @GrpcClient("orderlist")
    OrderListService orderService;

    /**
     * Sends a gRPC request with userId, objectIds, and the current date.
     */
    public Uni<Boolean> submitMissingOrder(int userId, List<Integer> objectIds, String date) {
        System.out.println("Preparing gRPC request for userId = " + userId
                + ", objectIds = " + objectIds + ", date = " + date);

        // Build the request
        GenerateOrderListRequest request = GenerateOrderListRequest.newBuilder()
                .setUserId(userId)
                .addAllObjectIds(objectIds)
                .setDate(date)
                .build();

        // Make the gRPC call
        // Return success status
        return orderService.generateOrderList(request)
                .onItem().invoke(response -> {
                    // Log response
                    System.out.println("Received response: " + response);
                })
                .onItem().transform(GenerateOrderListReply::getSuccess)
                .onFailure().invoke(throwable -> {
                    // Log errors
                    System.err.println("Error in submitMissingOrder gRPC call: " + throwable.getMessage());
                });
    }
}
