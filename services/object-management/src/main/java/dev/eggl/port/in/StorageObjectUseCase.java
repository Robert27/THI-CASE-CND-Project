package dev.eggl.port.in;

import dev.eggl.domain.model.StorageObject;

import java.util.List;

public interface StorageObjectUseCase {
    List<StorageObject> findAll(String token);

    StorageObject create(String name, String description, Integer categoryId, String reorderUrl, Integer quantity,
                         Integer weekDay, String token);

    StorageObject update(Integer id, String name, String description, Integer categoryId, String reorderUrl,
                         Integer quantity, Integer weekDay, String token);

    StorageObject delete(Integer id, String token);
}
