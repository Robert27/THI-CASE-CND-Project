package dev.eggl.port.in;

import dev.eggl.domain.model.StorageObject;

import java.util.List;
import java.util.Map;

public interface InternalStorageObjectUseCase {
    Map<Integer, List<Integer>> findAllDayUsers(Integer weekday, List<Integer> userIds);

    List<StorageObject> findByIds(List<Integer> ids);
}
