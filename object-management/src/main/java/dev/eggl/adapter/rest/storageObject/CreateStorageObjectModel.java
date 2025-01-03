package dev.eggl.adapter.rest.storageObject;

import dev.eggl.domain.model.StorageObject;

public record CreateStorageObjectModel(String name, String description, Integer categoryId, String reorderUrl) {
    public StorageObject toDomainModel() {
        return new StorageObject(
                null,
                name,
                description,
                categoryId,
                reorderUrl
        );
    }
}
