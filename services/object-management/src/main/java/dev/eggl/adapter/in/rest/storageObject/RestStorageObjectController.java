package dev.eggl.adapter.in.rest.storageObject;

import dev.eggl.adapter.in.rest.storageObject.dto.DeleteStorageObjectResponse;
import dev.eggl.adapter.in.rest.storageObject.dto.ListStorageObjectResponse;
import dev.eggl.adapter.in.rest.storageObject.dto.StorageObjectResponse;
import dev.eggl.adapter.in.rest.storageObject.dto.UpsertStorageObjectRequest;
import dev.eggl.domain.model.StorageObject;
import dev.eggl.port.in.StorageObjectUseCase;
import io.grpc.StatusRuntimeException;
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
public class RestStorageObjectController {
    private final StorageObjectUseCase listStorageObjectUseCase;

    public RestStorageObjectController(StorageObjectUseCase listStorageObjectUseCase) {
        this.listStorageObjectUseCase = listStorageObjectUseCase;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response create(UpsertStorageObjectRequest request, @Context HttpHeaders headers) {

        try {
            String token = extractJwt(headers);
            StorageObject created = listStorageObjectUseCase.create(
                    request.name(),
                    request.description(),
                    request.categoryId(),
                    request.reorderUrl(),
                    request.quantity(),
                    request.weekday(),
                    token);
            return Response.status(Response.Status.CREATED)
                    .entity(StorageObjectResponse.fromDomain(created))
                    .build();
        } catch (Exception e) {
            switch (e) {
                case StatusRuntimeException statusRuntimeException -> throw serverErrorException(
                        Response.Status.SERVICE_UNAVAILABLE, "URL Validation not available");
                case AuthenticationFailedException authenticationFailedException -> throw clientErrorException(
                        Response.Status.UNAUTHORIZED, e.getMessage());
                case IllegalArgumentException illegalArgumentException -> throw clientErrorException(
                        Response.Status.BAD_REQUEST, e.getMessage());
                default -> {
                    System.out.println(e.getMessage());
                    throw serverErrorException(
                            Response.Status.INTERNAL_SERVER_ERROR, "Error while creating storage object");
                }
            }
        }


    }

    @GET
    public Response findAll(@Context HttpHeaders headers) {
        try {
            String token = extractJwt(headers);
            List<StorageObject> storageObjects = listStorageObjectUseCase.findAll(token);
            return Response.status(Response.Status.OK)
                    .entity(ListStorageObjectResponse.fromDomain(storageObjects))
                    .build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (e instanceof AuthenticationFailedException) {
                throw clientErrorException(
                        Response.Status.UNAUTHORIZED, e.getMessage());
            }
            throw serverErrorException(
                    Response.Status.INTERNAL_SERVER_ERROR, "Error while fetching storage objects");
        }
    }

    @PATCH
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response update(@PathParam("id") Integer id, UpsertStorageObjectRequest request,
                           @Context HttpHeaders headers) {

        try {
            String token = extractJwt(headers);
            StorageObject updated = listStorageObjectUseCase.update(
                    id,
                    request.name(),
                    request.description(),
                    request.categoryId(),
                    request.reorderUrl(),
                    request.quantity(),
                    request.weekday(),
                    token);
            return Response.status(Response.Status.OK)
                    .entity(StorageObjectResponse.fromDomain(updated))
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
                    .entity(DeleteStorageObjectResponse.fromDomain(deleted))
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
