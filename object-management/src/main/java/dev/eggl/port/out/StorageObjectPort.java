package dev.eggl.port.out;

import dev.eggl.domain.model.StorageObject;

import java.util.List;

public interface StorageObjectPort {
    void save(StorageObject storageObject);
    List<StorageObject> findAll();

}
