package dev.eggl.application.service.object;

import dev.eggl.domain.model.StorageObject;
import dev.eggl.port.in.category.CategoryListUseCase;
import dev.eggl.port.in.storageObject.ListStorageObjectUseCase;
import dev.eggl.port.out.StorageObjectPort;
import dev.eggl.port.out.UrlValidationPort;

import java.util.Date;
import java.util.List;

public class StorageObjectService implements ListStorageObjectUseCase {
    private final StorageObjectPort storageObjectPort;
    private final CategoryListUseCase categoryListUseCase;
    private final UrlValidationPort urlValidationPort;

    public StorageObjectService(StorageObjectPort storageObjectPort, CategoryListUseCase categoryListUseCase, UrlValidationPort urlValidationPort) {
        this.storageObjectPort = storageObjectPort;
        this.categoryListUseCase = categoryListUseCase;
        this.urlValidationPort = urlValidationPort;

    }

    @Override
    public List<StorageObject> findAll() {
        return storageObjectPort.findAll();
    }

    @Override
    public StorageObject create(String name, String description, Integer categoryId, String reorderUrl, Integer quantity, Integer interval)
            throws IllegalArgumentException {
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
        if (!categoryListUseCase.existsById(categoryId)) {
            throw new IllegalArgumentException("Category ID does not exist");
        }
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        if (interval == null || interval <= 0) {
            throw new IllegalArgumentException("Interval must be at least 1 minute");
        }
        // if there is already a storage object with the same link, throw an exception
        if (storageObjectPort.existsByUrl(reorderUrl)) {
            throw new IllegalArgumentException("Storage object with the same reorder URL already exists");
        }
        // if there is already a storage object with the same name and category, throw an exception
        if (storageObjectPort.existsByNameAndCategory(name, categoryId)) {
            throw new IllegalArgumentException("Storage object with the same name and category already exists");
        }
        // TODO: infer userId from jwt
        return storageObjectPort.save(new StorageObject(null, 1213, name, description, categoryId, reorderUrl, quantity, interval, new Date()));
    }

    @Override
    public StorageObject update(Integer id, String name, String description, Integer categoryId, String reorderUrl, Integer quantity, Integer interval)
            throws IllegalArgumentException {
        StorageObject existing = storageObjectPort.findById(id);
        if (existing == null) {
            throw new IllegalArgumentException("Storage object not found");
        }
        // TODO: infer userId from jwt
        return storageObjectPort.update(new StorageObject(id, 1213, name, description, categoryId, reorderUrl, quantity, interval, existing.getCreatedAt()));
    }

    @Override
    public StorageObject delete(Integer id) {
        return storageObjectPort.delete(id);
    }

    @Override
    public List<StorageObject> findByIds(List<Integer> ids) {
        return storageObjectPort.findByIds(ids);
    }
}
