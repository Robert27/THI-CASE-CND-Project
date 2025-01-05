package dev.eggl.adapter.persistence.jpa.storageObject;

import dev.eggl.domain.model.StorageObject;

import java.util.List;

final class StorageObjectMapper {
    private StorageObjectMapper() {

    }

    static StorageObjectJpaEntity toJpaEntity(StorageObject entity) {
        StorageObjectJpaEntity jpaEntity = new StorageObjectJpaEntity();
        jpaEntity.setUserId(entity.getUserId());
        jpaEntity.setId(entity.getId());
        jpaEntity.setName(entity.getName());
        jpaEntity.setDescription(entity.getDescription());
        jpaEntity.setCategoryId(entity.getCategoryId());
        jpaEntity.setReorderUrl(entity.getReorderUrl());
        jpaEntity.setQuantity(entity.getQuantity());
        jpaEntity.setInterval(entity.getInterval());
        jpaEntity.setCreatedAt(entity.getCreatedAt());
        return jpaEntity;
    }

    static StorageObject toDomainEntity(StorageObjectJpaEntity jpaEntity) {
        return new StorageObject(
                jpaEntity.getId(),
                jpaEntity.getUserId(),
                jpaEntity.getName(),
                jpaEntity.getDescription(),
                jpaEntity.getCategoryId(),
                jpaEntity.getReorderUrl(),
                jpaEntity.getQuantity(),
                jpaEntity.getInterval(),
                jpaEntity.getCreatedAt()
        );
    }

    static List<StorageObject> toDomainList(List<StorageObjectJpaEntity> jpaEntities) {
        return jpaEntities.stream().map(StorageObjectMapper::toDomainEntity).toList();
    }

}
