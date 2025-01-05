package dev.eggl.port.out;

import dev.eggl.domain.model.StorageObject;

import java.util.List;

public interface StorageObjectPort {
    StorageObject save(StorageObject storageObject);

    List<StorageObject> findAll();

    StorageObject findById(Integer id);

    Boolean existsByUrl(String reorderUrl);

    StorageObject update(StorageObject storageObject);

    StorageObject delete(Integer id);

    boolean existsByNameAndCategory(String name, Integer categoryId);
}
