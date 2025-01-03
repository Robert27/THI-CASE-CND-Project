package dev.eggl.adapter.rest.storageObject;

import dev.eggl.domain.model.StorageObject;

public record ListStorageObjectModel(Integer id, String name, String description, Integer categoryId,
                                     String reorderUrl) {
    public static ListStorageObjectModel fromDomainModel(StorageObject storageObject) {
        return new ListStorageObjectModel(storageObject.getId(), storageObject.getName(), storageObject.getDescription(), storageObject.getCategoryId(), storageObject.getReorderUrl());
    }
}
