package dev.eggl.adapter.rest.storageObject;

import dev.eggl.domain.model.Category;
import dev.eggl.domain.model.StorageObject;

public record StorageObjectModel(Integer id, String name, String description, Category category) {
    public static StorageObjectModel fromDomainModel(StorageObject storageObject) {
        return new StorageObjectModel(storageObject.getId(), storageObject.getName(), storageObject.getDescription(), new Category(storageObject.getCategory().getId(), storageObject.getCategory().getName(), storageObject.getCategory().getDescription()));
    }
}
