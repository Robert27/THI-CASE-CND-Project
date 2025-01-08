package dev.eggl.adapter.grpc;

import dev.eggl.domain.model.StorageObject;
import dev.eggl.port.in.ListStorageObjectUseCase;
import dev.eggl.storageObjects.StorageObjectsProto;
import dev.eggl.storageObjects.StorageService;
import io.quarkus.grpc.GrpcService;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;

import java.util.List;
import java.util.stream.Collectors;

@GrpcService
public class GrpcController implements StorageService {

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
        return Uni.createFrom().item(request.getUserId())
                .onItem().transformToUni(userId -> Uni.createFrom().item(() -> listStorageObjectUseCase.findAll(userId))
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

    private StorageObjectsProto.StorageObject toProto(StorageObject storageObject) {
        return StorageObjectsProto.StorageObject.newBuilder()
                .setId(storageObject.getId())
                .setName(storageObject.getName())
                .setDescription(storageObject.getDescription())
                .build();
    }
}
