package dev.eggl.adapter.grpc;

import dev.eggl.domain.model.StorageObject;
import dev.eggl.objects.ObjectService;
import dev.eggl.objects.StorageObjectsProto;
import dev.eggl.port.in.ListStorageObjectUseCase;
import io.quarkus.grpc.GrpcService;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;

import java.util.List;
import java.util.stream.Collectors;

@GrpcService
public class GrpcController implements ObjectService {

    private final ListStorageObjectUseCase listStorageObjectUseCase;

    public GrpcController(ListStorageObjectUseCase listStorageObjectUseCase) {
        this.listStorageObjectUseCase = listStorageObjectUseCase;
    }

    @Override
    public Uni<StorageObjectsProto.StorageObjectsReply> getStorageObjectsByIds(StorageObjectsProto.StorageObjectIdsRequest request) {
        return Uni.createFrom().item(request.getIdsList())
                .onItem().transformToUni(ids -> Uni.createFrom().item(() -> listStorageObjectUseCase.findByIds(ids))
                        .runSubscriptionOn(Infrastructure.getDefaultExecutor()))
                .onItem().transform(storageObjects -> {
                    List<StorageObjectsProto.StorageObject> protoStorageObjects = storageObjects.stream()
                            .map(this::toProto)
                            .collect(Collectors.toList());
                    return StorageObjectsProto.StorageObjectsReply.newBuilder()
                            .addAllStorageObjects(protoStorageObjects)
                            .build();
                });
    }

    @Override
    public Uni<StorageObjectsProto.StorageObjectsReply> getStorageObjectsByUserId(StorageObjectsProto.UserIdRequest request) {
        return null;
    }

    @Override
    public Uni<StorageObjectsProto.DayUsersReply> findAllDayUsers(StorageObjectsProto.DayUsersRequest request) {
        return Uni.createFrom().item(request)
                .onItem().transformToUni(req ->
                        // findAllDayUsers now returns a Map<Integer, List<StorageObject>>
                        Uni.createFrom().item(() -> listStorageObjectUseCase.findAllDayUsers(req.getWeekDay(), req.getUserIdsList()))
                                .runSubscriptionOn(Infrastructure.getDefaultExecutor())
                )
                .onItem().transform(storageObjectsByUser -> {
                    List<StorageObjectsProto.UserObjectIds> userObjectIdsList =
                            storageObjectsByUser.entrySet().stream() // Access key-value pairs
                                    .map(entry ->
                                            StorageObjectsProto.UserObjectIds.newBuilder()
                                                    .setUserId(entry.getKey()) // Set the user ID
                                                    .addAllObjectIds(
                                                            entry.getValue()

                                                    )
                                                    .build()
                                    )
                                    .collect(Collectors.toList());

                    return StorageObjectsProto.DayUsersReply.newBuilder()
                            .addAllUserObjectIds(userObjectIdsList)
                            .build();
                });
    }


    private StorageObjectsProto.StorageObject toProto(StorageObject storageObject) {
        StorageObjectsProto.StorageObject.Builder protoBuilder = StorageObjectsProto.StorageObject.newBuilder()
                .setId(storageObject.getId())
                .setName(storageObject.getName() != null ? storageObject.getName() : "")
                .setDescription(storageObject.getDescription() != null ? storageObject.getDescription() : "")
                .setCategoryId(storageObject.getCategoryId() != null ? storageObject.getCategoryId() : 0)
                .setReorderUrl(storageObject.getReorderUrl() != null ? storageObject.getReorderUrl() : "")
                .setQuantity(storageObject.getQuantity() != null ? storageObject.getQuantity() : 0)
                .setWeekday(storageObject.getWeekday() != null ? storageObject.getWeekday() : 0);

        return protoBuilder.build();
    }
}
