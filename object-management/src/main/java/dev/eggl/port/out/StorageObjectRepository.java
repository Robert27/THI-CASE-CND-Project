package dev.eggl.port.out;

import dev.eggl.domain.model.StorageObject;

import java.util.List;
import java.util.Map;

public interface StorageObjectRepository {
    StorageObject save(StorageObject storageObject);

    List<StorageObject> findAll(Integer userId);

    StorageObject findById(Integer id, Integer userId);

    List<StorageObject> findByIds(List<Integer> ids);

    Map<Integer, List<Integer>> findAllDayUsers(Integer weekDay, List<Integer> userIds);

    Boolean existsByUrl(String reorderUrl, Integer userId);

    StorageObject update(StorageObject storageObject);

    StorageObject delete(Integer id, Integer userId);

    boolean existsByNameAndCategory(String name, Integer categoryId, Integer userId);
}
