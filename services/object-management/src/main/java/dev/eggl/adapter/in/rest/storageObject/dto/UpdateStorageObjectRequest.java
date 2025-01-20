package dev.eggl.adapter.in.rest.storageObject;

public record UpdateStorageObjectRequest(
        String name,
        String description,
        Integer categoryId,
        String reorderUrl,
        Integer quantity,
        Integer weekday
) {
}
