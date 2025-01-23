package dev.eggl.adapter.in.user.graphql;

import dev.eggl.adapter.in.user.dto.storageObject.DeleteStorageObjectResponse;
import dev.eggl.adapter.in.user.dto.storageObject.ListStorageObjectResponse;
import dev.eggl.adapter.in.user.dto.storageObject.StorageObjectResponse;
import dev.eggl.adapter.in.user.dto.storageObject.UpsertStorageObjectRequest;
import dev.eggl.domain.model.StorageObject;
import dev.eggl.port.in.StorageObjectUseCase;
import io.vertx.ext.web.RoutingContext;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.GraphQLException;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Query;

import java.util.List;

@GraphQLApi
public class GqlStorageObjectController {

    private final StorageObjectUseCase storageObjectUseCase;
    @Inject
    RoutingContext routingContext;

    public GqlStorageObjectController(StorageObjectUseCase storageObjectUseCase) {
        this.storageObjectUseCase = storageObjectUseCase;
    }

    private String extractToken() throws GraphQLException {
        String authHeader = routingContext.request().getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        throw new GraphQLException("Authorization header must be provided");
    }

    @Query("items")
    public ListStorageObjectResponse getAllStorageObjects() throws GraphQLException {
        String token = extractToken();
        List<StorageObject> storageObjects = storageObjectUseCase.findAll(token);
        return ListStorageObjectResponse.fromDomain(storageObjects);
    }

    @Mutation("createItem")
    @Transactional
    public StorageObjectResponse createStorageObject(UpsertStorageObjectRequest input) throws GraphQLException {
        String token = extractToken();
        try {
            StorageObject created = storageObjectUseCase.create(
                    input.name(),
                    input.description(),
                    input.categoryId(),
                    input.reorderUrl(),
                    input.quantity(),
                    input.weekday(),
                    token);
            return StorageObjectResponse.fromDomain(created);
        } catch (Exception e) {
            throw new GraphQLException(e.getMessage());
        }
    }

    @Mutation("updateItem")
    @Transactional
    public StorageObjectResponse updateStorageObject(int id, UpsertStorageObjectRequest input) throws GraphQLException {
        String token = extractToken();
        try {
            StorageObject updated = storageObjectUseCase.update(
                    id,
                    input.name(),
                    input.description(),
                    input.categoryId(),
                    input.reorderUrl(),
                    input.quantity(),
                    input.weekday(),
                    token);
            return StorageObjectResponse.fromDomain(updated);
        } catch (Exception e) {
            throw new GraphQLException(e.getMessage());
        }
    }

    @Mutation("deleteItem")
    @Transactional
    public DeleteStorageObjectResponse deleteStorageObject(int id) throws GraphQLException {
        String token = extractToken();
        try {
            StorageObject deleted = storageObjectUseCase.delete(id, token);
            return DeleteStorageObjectResponse.fromDomain(deleted);
        } catch (Exception e) {
            throw new GraphQLException(e.getMessage());
        }
    }
}
