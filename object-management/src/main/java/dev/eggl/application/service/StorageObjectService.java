package dev.eggl.application.service;

import dev.eggl.domain.model.AuthenticatedUser;
import dev.eggl.domain.model.StorageObject;
import dev.eggl.port.in.AuthenticationUseCase;
import dev.eggl.port.in.ListStorageObjectUseCase;
import dev.eggl.port.out.CategoryPort;
import dev.eggl.port.out.StorageObjectPort;
import dev.eggl.port.out.UrlValidationPort;

import java.util.Date;
import java.util.List;

public class StorageObjectService implements ListStorageObjectUseCase {
    private final StorageObjectPort storageObjectPort;
    private final CategoryPort categoryPort;
    private final UrlValidationPort urlValidationPort;
    private final AuthenticationUseCase authenticationUseCase;

    public StorageObjectService(StorageObjectPort storageObjectPort, CategoryPort categoryPort, UrlValidationPort urlValidationPort, AuthenticationUseCase authenticationUseCase) {
        this.storageObjectPort = storageObjectPort;
        this.categoryPort = categoryPort;
        this.urlValidationPort = urlValidationPort;
        this.authenticationUseCase = authenticationUseCase;

    }

    @Override
    public List<StorageObject> findAll(
            String token
    ) {
        AuthenticatedUser user;

        user = authenticationUseCase.authenticate(token);

        return storageObjectPort.findAll(user.getUserId());
    }

    @Override
    public StorageObject create(String name, String description, Integer categoryId, String reorderUrl, Integer quantity, Integer interval, String token)
            throws IllegalArgumentException {
        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException("Authorization token must be provided");
        }
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name must be provided");
        }
        if (categoryId == null) {
            throw new IllegalArgumentException("Category ID must be provided");
        }
        if (reorderUrl == null || reorderUrl.isEmpty()) {
            throw new IllegalArgumentException("Reorder URL must be provided");
        }
        if (!urlValidationPort.validateUrl(reorderUrl)) {
            throw new IllegalArgumentException("Reorder URL is invalid or unreachable");
        }
        if (!categoryPort.existsById(categoryId)) {
            throw new IllegalArgumentException("Category ID does not exist");
        }
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        if (interval == null || interval <= 0) {
            throw new IllegalArgumentException("Interval must be at least 1 minute");
        }

        // try to authenticate the user
        AuthenticatedUser user;
        try {
            user = authenticationUseCase.authenticate(token);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid authorization token");
        }

        // if there is already a storage object with the same link, throw an exception
        if (storageObjectPort.existsByUrl(reorderUrl, user.getUserId())) {
            throw new IllegalArgumentException("Storage object with the same reorder URL already exists");
        }
        // if there is already a storage object with the same name and category, throw an exception
        if (storageObjectPort.existsByNameAndCategory(name, categoryId, user.getUserId())) {
            throw new IllegalArgumentException("Storage object with the same name and category already exists");
        }
        // TODO: infer userId from jwt
        return storageObjectPort.save(new StorageObject(null, user.getUserId(), name, description, categoryId, reorderUrl, quantity, interval, new Date()));
    }

    @Override
    public StorageObject update(Integer id, String name, String description, Integer categoryId, String reorderUrl, Integer quantity, Integer interval, String token)
            throws IllegalArgumentException {
        AuthenticatedUser user;
        try {
            user = authenticationUseCase.authenticate(token);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid authorization token");
        }
        StorageObject existing = storageObjectPort.findById(id, user.getUserId());
        if (existing == null) {
            throw new IllegalArgumentException("Storage object not found");
        }
        // TODO: infer userId from jwt
        return storageObjectPort.update(new StorageObject(id, 1213, name, description, categoryId, reorderUrl, quantity, interval, existing.getCreatedAt()));
    }

    @Override
    public StorageObject delete(Integer id, String token) {
        AuthenticatedUser user;
        try {
            user = authenticationUseCase.authenticate(token);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid authorization token");
        }
        return storageObjectPort.delete(id, user.getUserId());
    }

    @Override
    public List<StorageObject> findByIds(List<Integer> ids, String token) {
        AuthenticatedUser user;
        try {
            user = authenticationUseCase.authenticate(token);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid authorization token");
        }
        return storageObjectPort.findByIds(ids, user.getUserId());
    }
}
