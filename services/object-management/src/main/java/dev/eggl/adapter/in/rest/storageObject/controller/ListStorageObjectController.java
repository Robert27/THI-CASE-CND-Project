package dev.eggl.adapter.in.rest.storageObject.controller;

import dev.eggl.adapter.in.rest.storageObject.ListStorageObjectResponse;
import dev.eggl.adapter.in.rest.storageObject.StorageObjectResponse;
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
public class ListStorageObjectController {

    private final StorageObjectUseCase listStorageObjectUseCase;

    public ListStorageObjectController(StorageObjectUseCase listStorageObjectUseCase) {
        this.listStorageObjectUseCase = listStorageObjectUseCase;
    }

    @GET
    public List<StorageObjectResponse> findAll(@Context HttpHeaders headers) {
        try {
            String token = extractJwt(headers);
            List<StorageObject> storageObjects = listStorageObjectUseCase.findAll(token);
            return storageObjects.stream()
                    .map(StorageObjectResponse::fromDomain)
                    .toList();
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

}
