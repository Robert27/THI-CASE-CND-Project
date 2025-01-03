package dev.eggl.application.service.object;

import dev.eggl.domain.model.StorageObject;
import dev.eggl.port.in.category.CategoryListUseCase;
import dev.eggl.port.in.storageObject.ListStorageObjectUseCase;
import dev.eggl.port.out.StorageObjectPort;

import java.util.List;

public class StorageObjectService implements ListStorageObjectUseCase {
    private final StorageObjectPort storageObjectPort;
    private final CategoryListUseCase categoryListUseCase;

    public StorageObjectService(StorageObjectPort storageObjectPort, CategoryListUseCase categoryListUseCase) {
        this.storageObjectPort = storageObjectPort;
        this.categoryListUseCase = categoryListUseCase;
    }

    @Override
    public List<StorageObject> findAll() {
        return storageObjectPort.findAll();
    }

    @Override
    public StorageObject create(String name, String description, Integer categoryId, String reorderUrl)
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
        System.out.println(categoryId);
        System.out.println(categoryListUseCase.existsById(categoryId));
        if (!categoryListUseCase.existsById(categoryId)) {
            throw new IllegalArgumentException("Category ID does not exist");
        }

        return storageObjectPort.save(new StorageObject(null, name, description, categoryId, reorderUrl));
    }
}
