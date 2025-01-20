package dev.eggl.adapter.in.rest.storageObject.controller;

import dev.eggl.adapter.in.rest.storageObject.DeleteStorageObjectResponse;
import dev.eggl.domain.model.StorageObject;
import dev.eggl.port.in.StorageObjectUseCase;
import io.quarkus.security.AuthenticationFailedException;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import static dev.eggl.adapter.in.rest.common.ControllerCommons.*;

@Path("/item")
@Produces(MediaType.APPLICATION_JSON)
public class DeleteObjectController {

    private final StorageObjectUseCase listStorageObjectUseCase;

    public DeleteObjectController(StorageObjectUseCase listStorageObjectUseCase) {
        this.listStorageObjectUseCase = listStorageObjectUseCase;
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
