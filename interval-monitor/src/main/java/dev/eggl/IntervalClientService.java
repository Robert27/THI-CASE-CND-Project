package dev.eggl;

import dev.eggl.intervals.IntervalService;
import dev.eggl.intervals.IntervalSubmissionRequest;
import dev.eggl.intervals.IntervalSubmissionResponse;
import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.Instant;
import java.util.List;

@ApplicationScoped
public class IntervalClientService {

    @GrpcClient("intervals")
    IntervalService orderService;

    /**
     * Sends a gRPC request with userId, objectIds, and the current date.
     */
    public Uni<Boolean> submitMissingOrder(int userId, List<Integer> objectIds, Instant date) {
        System.out.println("Preparing gRPC request for userId = " + userId
                + ", objectIds = " + objectIds + ", date = " + date);

        // Convert the date to milliseconds
        long millis = date.toEpochMilli();

        // Build the request
        IntervalSubmissionRequest request = IntervalSubmissionRequest.newBuilder()
                .setUserId(userId)
                .addAllObjectIds(objectIds)
                .setDate(millis)
                .build();

        // Make the gRPC call
        // Return success status
        return orderService.intervalSubmission(request)
                .onItem().invoke(response -> {
                    // Log response
                    System.out.println("Received response: " + response);
                })
                .onItem().transform(IntervalSubmissionResponse::getSuccess)
                .onFailure().invoke(throwable -> {
                    // Log errors
                    System.err.println("Error in submitMissingOrder gRPC call: " + throwable.getMessage());
                });
    }
}
