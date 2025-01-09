package dev.eggl;

import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ObjectClientService {

    @GrpcClient("objects")
    ObjectService objects;

    public Uni<String> getObjects(Integer userId) {
        return objects.getStorageObjectsByUserId(UserIdRequest.newBuilder().setUserId(userId).build())
                .onItem().transform(reply -> reply.getStorageObjectsList().toString());
    }
}
