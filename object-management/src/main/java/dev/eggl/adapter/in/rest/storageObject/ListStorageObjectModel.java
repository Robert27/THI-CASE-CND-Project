package dev.eggl.adapter.in.rest.storageObject;

import dev.eggl.domain.model.StorageObject;

public record ListStorageObjectModel(Integer id, String name, String description, Integer categoryId,
                                     String reorderUrl, Integer quantity, Integer weekday) {
    public static ListStorageObjectModel fromDomainModel(StorageObject storageObject) {
        return new ListStorageObjectModel(storageObject.getId(), storageObject.getName(), storageObject.getDescription(), storageObject.getCategoryId(), storageObject.getReorderUrl(), storageObject.getQuantity(), storageObject.getWeekday());
    }
}
