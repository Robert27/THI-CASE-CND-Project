package dev.eggl.port.in.storageObject;

import dev.eggl.domain.model.StorageObject;

import java.util.List;

public interface ListStorageObjectUseCase {
    List<StorageObject> findAll();

    StorageObject create(String name, String description, Integer categoryId, String reorderUrl, Integer quantity, Integer interval);

    StorageObject update(Integer id, String name, String description, Integer categoryId, String reorderUrl, Integer quantity, Integer interval);

    StorageObject delete(Integer id);

    List<StorageObject> findByIds(List<Integer> ids);
}
