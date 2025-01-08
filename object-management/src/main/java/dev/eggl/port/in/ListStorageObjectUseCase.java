package dev.eggl.port.in;

import dev.eggl.domain.model.StorageObject;

import java.util.List;

public interface ListStorageObjectUseCase {
    List<StorageObject> findAll(String token);

    List<StorageObject> findAll(Integer userId);

    StorageObject create(String name, String description, Integer categoryId, String reorderUrl, Integer quantity, String token);

    StorageObject update(Integer id, String name, String description, Integer categoryId, String reorderUrl, Integer quantity, String token);

    StorageObject delete(Integer id, String token);

    List<StorageObject> findByIds(List<Integer> ids);
}
