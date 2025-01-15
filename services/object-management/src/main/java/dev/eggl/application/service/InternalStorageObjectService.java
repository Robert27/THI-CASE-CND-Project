package dev.eggl.application.service;

import dev.eggl.domain.model.StorageObject;
import dev.eggl.port.in.InternalStorageObjectUseCase;
import dev.eggl.port.out.StorageObjectRepository;

import java.util.List;
import java.util.Map;

public class InternalStorageObjectService implements InternalStorageObjectUseCase {
    private final StorageObjectRepository storageObjectRepository;

    public InternalStorageObjectService(StorageObjectRepository storageObjectRepository) {
        this.storageObjectRepository = storageObjectRepository;
    }

    @Override
    public List<StorageObject> findByIds(List<Integer> ids) {
        return storageObjectRepository.findByIds(ids);
    }

    @Override
    public Map<Integer, List<Integer>> findAllDayUsers(Integer weekDay, List<Integer> userIds) {
        return storageObjectRepository.findAllDayUsers(weekDay, userIds);
    }

}
