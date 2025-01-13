package dev.eggl.grpc;

import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import dev.eggl.users.UserIdsRequest;
import dev.eggl.users.GrpcUserService;

import java.util.List;

@ApplicationScoped
public class UserClientService {

    @GrpcClient("users")
    GrpcUserService grpcUserService;

    public Uni<UserIdsResponse> getUserIds() {
        // Create the request
        UserIdsRequest request = UserIdsRequest.newBuilder().build();

        // Log the request
        System.out.println("Starting gRPC call to getUserIds");

        // Send the request to the gRPC server and transform the response
        return grpcUserService.getUserIds(request)
                .onItem().invoke(reply -> {
                    // Log the received reply
                    System.out.println("Received gRPC reply: " + reply);
                })
                .onItem().transform(reply -> {
                    // Log before transformation
                    System.out.println("Transforming gRPC reply to custom DTO");

                    // Transform the response into a custom DTO
                    List<Integer> userIds = reply.getUserIdsList();
                    UserIdsResponse response = new UserIdsResponse(userIds);

                    // Log the final transformed response
                    System.out.println("Final transformed response: " + response);
                    return response;
                })
                .onFailure().invoke(throwable -> {
                    // Error handling and logging
                    System.err.println("Error occurred during gRPC call: " + throwable.getMessage());
                    throwable.printStackTrace();
                });
    }

    // DTO for the JSON response
    public static class UserIdsResponse {
        public List<Integer> userIds;

        public UserIdsResponse(List<Integer> userIds) {
            this.userIds = userIds;
        }

        @Override
        public String toString() {
            return "UserIdsResponse{" +
                    "userIds=" + userIds +
                    '}';
        }
    }
}
