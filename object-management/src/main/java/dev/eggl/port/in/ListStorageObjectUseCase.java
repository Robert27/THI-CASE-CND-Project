package dev.eggl.port.in;

import dev.eggl.domain.model.StorageObject;

import java.util.List;
import java.util.Map;

public interface ListStorageObjectUseCase {
    List<StorageObject> findAll(String token);

    Map<Integer, List<Integer>> findAllDayUsers(Integer weekday, List<Integer> userIds);

    StorageObject create(String name, String description, Integer categoryId, String reorderUrl, Integer quantity,
                         Integer weekDay, String token);

    StorageObject update(Integer id, String name, String description, Integer categoryId, String reorderUrl,
                         Integer quantity, Integer weekday, String token);

    StorageObject delete(Integer id, String token);

    List<StorageObject> findByIds(List<Integer> ids);
}
