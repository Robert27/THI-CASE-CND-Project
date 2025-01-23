package dev.eggl.adapter.in.user.dto.storageObject;

import dev.eggl.domain.model.StorageObject;

import java.util.List;

public record ListStorageObjectResponse(
        List<StorageObjectResponse> items,
        int totalItems,
        boolean hasMore,
        String nextPageToken
) {
    public static ListStorageObjectResponse fromDomain(List<StorageObject> objects) {
        List<StorageObjectResponse> items = objects.stream()
                .map(StorageObjectResponse::fromDomain)
                .toList();

        return new ListStorageObjectResponse(
                items,
                items.size(),
                false,
                null
        );
    }
}
