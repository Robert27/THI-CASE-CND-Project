package dev.eggl.adapter.rest.storageObject;

import dev.eggl.domain.model.StorageObject;
import dev.eggl.port.in.storageObject.ListStorageObjectUseCase;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

import static dev.eggl.adapter.rest.common.ControllerCommons.clientErrorException;
import static dev.eggl.adapter.rest.common.ControllerCommons.serverErrorException;

@Path("/object")
@Produces(MediaType.APPLICATION_JSON)
public class ListStorageObjectController {

    private final ListStorageObjectUseCase listStorageObjectUseCase;

    public ListStorageObjectController(ListStorageObjectUseCase listStorageObjectUseCase) {
        this.listStorageObjectUseCase = listStorageObjectUseCase;
    }

    @GET
    public List<ListStorageObjectModel> findAll() {
        List<StorageObject> storageObjects;
        try {
            storageObjects = listStorageObjectUseCase.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error while fetching storage objects", e);
        }
        return storageObjects.stream()
                .map(ListStorageObjectModel::fromDomainModel)
                .toList();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response create(CreateStorageObjectModel createStorageObjectModel) {
        try {
            StorageObject created = listStorageObjectUseCase.create(
                    createStorageObjectModel.name(),
                    createStorageObjectModel.description(),
                    createStorageObjectModel.categoryId(),
                    createStorageObjectModel.reorderUrl()
            );
            return Response.status(Response.Status.CREATED)
                    .entity(ListStorageObjectModel.fromDomainModel(created))
                    .build();
        } catch (IllegalArgumentException e) {
            throw clientErrorException(
                    Response.Status.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw serverErrorException(
                    Response.Status.INTERNAL_SERVER_ERROR, "Error while creating storage object");

        }
    }
}
