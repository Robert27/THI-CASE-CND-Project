package dev.eggl.adapter.in.user.dto.storageObject;

public record UpsertStorageObjectRequest(
        String name,
        String description,
        Integer categoryId,
        String reorderUrl,
        Integer quantity,
        Integer weekday
) {
}
