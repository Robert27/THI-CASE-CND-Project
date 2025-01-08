package dev.eggl.application.service;

import dev.eggl.domain.model.AuthenticatedUser;
import dev.eggl.domain.model.StorageObject;
import dev.eggl.port.in.AuthenticationUseCase;
import dev.eggl.port.in.ListStorageObjectUseCase;
import dev.eggl.port.out.CategoryRepository;
import dev.eggl.port.out.StorageObjectRepository;
import dev.eggl.port.out.UrlValidationPort;

import java.util.Date;
import java.util.List;

public class StorageObjectService implements ListStorageObjectUseCase {
    private final StorageObjectRepository storageObjectRepository;
    private final CategoryRepository categoryRepository;
    private final UrlValidationPort urlValidationPort;
    private final AuthenticationUseCase authenticationUseCase;

    public StorageObjectService(StorageObjectRepository storageObjectRepository, CategoryRepository categoryRepository, UrlValidationPort urlValidationPort, AuthenticationUseCase authenticationUseCase) {
        this.storageObjectRepository = storageObjectRepository;
        this.categoryRepository = categoryRepository;
        this.urlValidationPort = urlValidationPort;
        this.authenticationUseCase = authenticationUseCase;

    }

    @Override
    public List<StorageObject> findAll(
            String token
    ) {
        AuthenticatedUser user;

        user = authenticationUseCase.authenticate(token);

        return storageObjectRepository.findAll(user.getUserId());
    }

    @Override
    public List<StorageObject> findAll(Integer userId) {
        return storageObjectRepository.findAll(userId);
    }

    @Override
    public StorageObject create(String name, String description, Integer categoryId, String reorderUrl, Integer quantity, String token)
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
        // TODO: validate URL
//        if (!urlValidationPort.validateUrl(reorderUrl)) {
//            throw new IllegalArgumentException("Reorder URL is invalid or unreachable");
//        }
        if (!categoryRepository.existsById(categoryId)) {
            throw new IllegalArgumentException("Category ID does not exist");
        }
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        // try to authenticate the user
        AuthenticatedUser user;
        try {
            user = authenticationUseCase.authenticate(token);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid authorization token");
        }

        // if there is already a storage object with the same link, throw an exception
        if (storageObjectRepository.existsByUrl(reorderUrl, user.getUserId())) {
            throw new IllegalArgumentException("Storage object with the same reorder URL already exists");
        }
        // if there is already a storage object with the same name and category, throw an exception
        if (storageObjectRepository.existsByNameAndCategory(name, categoryId, user.getUserId())) {
            throw new IllegalArgumentException("Storage object with the same name and category already exists");
        }
        return storageObjectRepository.save(new StorageObject(null, user.getUserId(), name, description, categoryId, reorderUrl, quantity, new Date()));
    }

    @Override
    public StorageObject update(Integer id, String name, String description, Integer categoryId, String reorderUrl, Integer quantity, String token)
            throws IllegalArgumentException {
        AuthenticatedUser user;
        try {
            user = authenticationUseCase.authenticate(token);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid authorization token");
        }
        StorageObject existing = storageObjectRepository.findById(id, user.getUserId());
        if (existing == null) {
            throw new IllegalArgumentException("Storage object not found");
        }
        // TODO: infer userId from jwt
        return storageObjectRepository.update(new StorageObject(id, 1213, name, description, categoryId, reorderUrl, quantity, existing.getCreatedAt()));
    }

    @Override
    public StorageObject delete(Integer id, String token) {
        AuthenticatedUser user;
        try {
            user = authenticationUseCase.authenticate(token);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid authorization token");
        }
        return storageObjectRepository.delete(id, user.getUserId());
    }

    @Override
    public List<StorageObject> findByIds(List<Integer> ids) {
        return storageObjectRepository.findByIds(ids);
    }


}
