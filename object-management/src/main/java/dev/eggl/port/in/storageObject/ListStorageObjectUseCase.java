package dev.eggl.port.in.storageObject;

import dev.eggl.domain.model.StorageObject;

import java.util.List;

public interface ListStorageObjectUseCase {
    List<StorageObject> findAll();
    StorageObject create(String name, String description, Integer categoryId, String reorderUrl);
}
