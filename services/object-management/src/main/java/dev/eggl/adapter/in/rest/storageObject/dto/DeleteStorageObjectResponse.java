package dev.eggl.adapter.in.rest.storageObject.dto;

import dev.eggl.domain.model.StorageObject;

public record DeleteStorageObjectResponse(
        Integer id,
        String name,
        boolean success
) {
    public static DeleteStorageObjectResponse fromDomain(StorageObject object) {
        return new DeleteStorageObjectResponse(
                object.getId(),
                object.getName(),
                true
        );
    }
}
