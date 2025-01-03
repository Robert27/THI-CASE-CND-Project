package dev.eggl.adapter.rest.storageObject;

import dev.eggl.domain.model.StorageObject;
import dev.eggl.port.in.storageObject.ListStorageObjectUseCase;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/object")
@Produces(MediaType.APPLICATION_JSON)
public class StorageObjectController {

    private final ListStorageObjectUseCase listStorageObjectUseCase;

    public StorageObjectController(ListStorageObjectUseCase listStorageObjectUseCase) {
        this.listStorageObjectUseCase = listStorageObjectUseCase;
    }

    @GET
    public List<StorageObjectModel> findAll() {
        List<StorageObject> storageObjects;
        try {
            storageObjects = listStorageObjectUseCase.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error while fetching storage objects", e);
        }
        return storageObjects.stream()
                .map(StorageObjectModel::fromDomainModel)
                .toList();
    }
}
