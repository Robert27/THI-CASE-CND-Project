package dev.eggl.adapter.in.rest.storageObject.controller;

import dev.eggl.adapter.in.rest.storageObject.StorageObjectResponse;
import dev.eggl.adapter.in.rest.storageObject.UpdateStorageObjectRequest;
import dev.eggl.domain.model.StorageObject;
import dev.eggl.port.in.StorageObjectUseCase;
import io.quarkus.security.AuthenticationFailedException;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import static dev.eggl.adapter.in.rest.common.ControllerCommons.*;

@Path("/item")
@Produces(MediaType.APPLICATION_JSON)
public class UpdateStorageObjectController {

    private final StorageObjectUseCase listStorageObjectUseCase;


    public UpdateStorageObjectController(StorageObjectUseCase listStorageObjectUseCase) {
        this.listStorageObjectUseCase = listStorageObjectUseCase;
    }

    @PATCH
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response update(@PathParam("id") Integer id, UpdateStorageObjectRequest request,
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

}
