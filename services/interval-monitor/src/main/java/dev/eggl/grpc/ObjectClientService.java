package dev.eggl.grpc;

import dev.eggl.objects.ObjectService;
import dev.eggl.objects.StorageObjectsProto;
import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ObjectClientService {

    @GrpcClient("objects")
    ObjectService objects;

    public Uni<DayUsersResponse> findAllDayUsers(int weekDay, List<Integer> userIds) {
        StorageObjectsProto.DayUsersRequest request = StorageObjectsProto.DayUsersRequest.newBuilder()
                .setWeekDay(weekDay)
                .addAllUserIds(userIds)
                .build();

        return objects.findAllDayUsers(request)
                .onItem().invoke(reply -> {

                })
                .onItem().transform(reply -> {

                    List<UserObjectIds> userObjectIdsList = reply.getUserObjectIdsList().stream()
                            .map(protoUserObjectIds -> new UserObjectIds(
                                    protoUserObjectIds.getUserId(),
                                    protoUserObjectIds.getObjectIdsList()))
                            .collect(Collectors.toList());

                    return new DayUsersResponse(userObjectIdsList);
                })
                .onFailure().invoke(throwable -> {
                    System.err.println("Error occurred during gRPC call: " + throwable.getMessage());
                });
    }

    public static class DayUsersResponse {
        public List<UserObjectIds> userObjectIds;

        public DayUsersResponse(List<UserObjectIds> userObjectIds) {
            this.userObjectIds = userObjectIds;
        }

        @Override
        public String toString() {
            return "DayUsersResponse{" +
                    "userObjectIds=" + userObjectIds +
                    '}';
        }
    }

    public static class UserObjectIds {
        public int userId;
        public List<Integer> objectIds;

        public UserObjectIds(int userId, List<Integer> objectIds) {
            this.userId = userId;
            this.objectIds = objectIds;
        }

        @Override
        public String toString() {
            return "UserObjectIds{" +
                    "userId=" + userId +
                    ", objectIds=" + objectIds +
                    '}';
        }
    }
}
