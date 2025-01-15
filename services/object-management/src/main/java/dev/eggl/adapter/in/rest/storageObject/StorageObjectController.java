package dev.eggl.adapter.in.rest.storageObject;

import dev.eggl.domain.model.StorageObject;
import dev.eggl.port.in.StorageObjectUseCase;
import io.quarkus.security.AuthenticationFailedException;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

import static dev.eggl.adapter.in.rest.common.ControllerCommons.*;

@Path("/item")
@Produces(MediaType.APPLICATION_JSON)
public class StorageObjectController {

    private final StorageObjectUseCase listStorageObjectUseCase;

    public StorageObjectController(StorageObjectUseCase listStorageObjectUseCase) {
        this.listStorageObjectUseCase = listStorageObjectUseCase;
    }

    @GET
    public List<ListStorageObjectModel> findAll(@Context HttpHeaders headers) {

        List<StorageObject> storageObjects;

        try {
            String token = extractJwt(headers);
            storageObjects = listStorageObjectUseCase.findAll(token);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (e instanceof AuthenticationFailedException) {
                throw clientErrorException(
                        Response.Status.UNAUTHORIZED, e.getMessage());
            }
            throw serverErrorException(
                    Response.Status.INTERNAL_SERVER_ERROR, "Error while fetching storage objects");
        }
        return storageObjects.stream()
                .map(ListStorageObjectModel::fromDomainModel)
                .toList();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response create(CreateStorageObjectModel createStorageObjectModel, @Context HttpHeaders headers) {

        try {
            String token = extractJwt(headers);
            StorageObject created = listStorageObjectUseCase.create(
                    createStorageObjectModel.name(),
                    createStorageObjectModel.description(),
                    createStorageObjectModel.categoryId(),
                    createStorageObjectModel.reorderUrl(),
                    createStorageObjectModel.quantity(),
                    createStorageObjectModel.weekday(),
                    token);
            return Response.status(Response.Status.CREATED)
                    .entity(ListStorageObjectModel.fromDomainModel(created))
                    .build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (e instanceof AuthenticationFailedException) {
                throw clientErrorException(
                        Response.Status.UNAUTHORIZED, e.getMessage());
            } else if (e instanceof IllegalArgumentException) {
                throw clientErrorException(
                        Response.Status.BAD_REQUEST, e.getMessage());

            } else {
                System.out.println(e.getMessage());
                throw serverErrorException(
                        Response.Status.INTERNAL_SERVER_ERROR, "Error while creating storage object");
            }
        }

    }

    @PATCH
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response update(@PathParam("id") Integer id, CreateStorageObjectModel createStorageObjectModel,
                           @Context HttpHeaders headers) {

        try {
            String token = extractJwt(headers);
            StorageObject updated = listStorageObjectUseCase.update(
                    id,
                    createStorageObjectModel.name(),
                    createStorageObjectModel.description(),
                    createStorageObjectModel.categoryId(),
                    createStorageObjectModel.reorderUrl(),
                    createStorageObjectModel.quantity(),
                    createStorageObjectModel.weekday(),
                    token);
            return Response.status(Response.Status.OK)
                    .entity(ListStorageObjectModel.fromDomainModel(updated))
                    .build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (e instanceof AuthenticationFailedException) {
                throw clientErrorException(
                        Response.Status.UNAUTHORIZED, e.getMessage());
            } else if (e instanceof IllegalArgumentException) {
                throw clientErrorException(
                        Response.Status.BAD_REQUEST, e.getMessage());

            } else {
                throw serverErrorException(
                        Response.Status.INTERNAL_SERVER_ERROR, "Error while updating storage object");
            }
        }
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") Integer id, @Context HttpHeaders headers) {

        try {
            String token = extractJwt(headers);
            StorageObject deleted = listStorageObjectUseCase.delete(id, token);
            return Response.status(Response.Status.OK)
                    .entity(ListStorageObjectModel.fromDomainModel(deleted))
                    .build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (e instanceof AuthenticationFailedException) {
                throw clientErrorException(
                        Response.Status.UNAUTHORIZED, e.getMessage());
            } else if (e instanceof IllegalArgumentException) {
                throw clientErrorException(
                        Response.Status.BAD_REQUEST, e.getMessage());

            } else {
                throw serverErrorException(
                        Response.Status.INTERNAL_SERVER_ERROR, "Error while deleting storage object");
            }
        }

    }
}
