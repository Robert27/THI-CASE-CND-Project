package dev.eggl.adapter.rest.storageObject;

import dev.eggl.domain.model.StorageObject;

public record CreateStorageObjectModel(String name, Integer userId, String description, Integer categoryId,
                                       String reorderUrl, Integer quantity, Integer interval) {
    public StorageObject toDomainModel() {
        return new StorageObject(
                null,
                userId,
                name,
                description,
                categoryId,
                reorderUrl,
                quantity,
                interval,
                null
        );
    }
}
