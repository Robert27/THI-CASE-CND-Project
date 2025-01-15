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
        UserIdsRequest request = UserIdsRequest.newBuilder().build();

        // Send the request to the gRPC server and transform the response
        return grpcUserService.getUserIds(request)
                .onItem().invoke(reply -> {

                })
                .onItem().transform(reply -> {
                    // Transform the response into a custom DTO
                    List<Integer> userIds = reply.getUserIdsList();
                    return new UserIdsResponse(userIds);
                })
                .onFailure().invoke(throwable -> {
                    // Error handling and logging
                    System.err.println("Error occurred during gRPC call: " + throwable.getMessage());
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
