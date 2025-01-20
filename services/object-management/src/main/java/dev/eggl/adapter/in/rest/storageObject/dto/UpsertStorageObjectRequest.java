package dev.eggl.adapter.in.rest.storageObject.dto;

public record UpsertStorageObjectRequest(
        String name,
        String description,
        Integer categoryId,
        String reorderUrl,
        Integer quantity,
        Integer weekday
) {
}
