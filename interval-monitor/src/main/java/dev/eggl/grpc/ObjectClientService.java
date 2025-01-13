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
        // Log der Anfrageparameter
        System.out.println("Starting gRPC call to findAllDayUsers");
        System.out.println("WeekDay: " + weekDay);
        System.out.println("UserIds: " + userIds);

        // Anfrage erstellen
        StorageObjectsProto.DayUsersRequest request = StorageObjectsProto.DayUsersRequest.newBuilder()
                .setWeekDay(weekDay) // Wochentag setzen
                .addAllUserIds(userIds) // Benutzer-IDs hinzufügen
                .build();

        System.out.println("Built gRPC request: " + request);

        // Anfrage an den gRPC-Server senden und Antwort transformieren
        return objects.findAllDayUsers(request)
                .onItem().invoke(reply -> {
                    // Log der erhaltenen Antwort
                    System.out.println("Received gRPC reply: " + reply);
                })
                .onItem().transform(reply -> {
                    // Log vor der Transformation
                    System.out.println("Transforming gRPC reply to custom DTO");

                    // Umwandlung der Antwort in benutzerdefinierte JSON-Struktur
                    List<UserObjectIds> userObjectIdsList = reply.getUserObjectIdsList().stream()
                            .map(protoUserObjectIds -> {
                                System.out.println("Processing userId: " + protoUserObjectIds.getUserId());
                                System.out.println("ObjectIds: " + protoUserObjectIds.getObjectIdsList());
                                return new UserObjectIds(
                                        protoUserObjectIds.getUserId(),
                                        protoUserObjectIds.getObjectIdsList()
                                );
                            })
                            .collect(Collectors.toList());

                    DayUsersResponse response = new DayUsersResponse(userObjectIdsList);
                    // Log der endgültigen JSON-Struktur
                    System.out.println("Final transformed response: " + response);
                    return response;
                })
                .onFailure().invoke(throwable -> {
                    // Fehlerbehandlung und Logging
                    System.err.println("Error occurred during gRPC call: " + throwable.getMessage());
                    throwable.printStackTrace();
                });
    }
    // DTO für die JSON-Antwort
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
