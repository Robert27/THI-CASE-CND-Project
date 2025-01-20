package dev.eggl.adapter.in.rest.storageObject.controller;

import dev.eggl.adapter.in.rest.storageObject.CreateStorageObjectRequest;
import dev.eggl.adapter.in.rest.storageObject.StorageObjectResponse;
import dev.eggl.domain.model.StorageObject;
import dev.eggl.port.in.StorageObjectUseCase;
import io.grpc.StatusRuntimeException;
import io.quarkus.security.AuthenticationFailedException;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import static dev.eggl.adapter.in.rest.common.ControllerCommons.*;

@Path("/item")
@Produces(MediaType.APPLICATION_JSON)
public class CreateStorageObjectController {
    private final StorageObjectUseCase listStorageObjectUseCase;
    
    public CreateStorageObjectController(StorageObjectUseCase listStorageObjectUseCase) {
        this.listStorageObjectUseCase = listStorageObjectUseCase;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response create(CreateStorageObjectRequest request, @Context HttpHeaders headers) {

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

}
