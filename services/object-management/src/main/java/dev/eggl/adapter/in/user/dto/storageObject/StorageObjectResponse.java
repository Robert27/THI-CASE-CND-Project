package dev.eggl.adapter.in.user.dto.storageObject;

import dev.eggl.domain.model.StorageObject;

import java.util.Date;

public record StorageObjectResponse(
        Integer id,
        String name,
        String description,
        Integer categoryId,
        String reorderUrl,
        Integer quantity,
        Integer weekday,
        Date createdAt
) {
    public static StorageObjectResponse fromDomain(StorageObject object) {
        return new StorageObjectResponse(
                object.getId(),
                object.getName(),
                object.getDescription(),
                object.getCategoryId(),
                object.getReorderUrl(),
                object.getQuantity(),
                object.getWeekday(),
                object.getCreatedAt()
        );
    }
}
