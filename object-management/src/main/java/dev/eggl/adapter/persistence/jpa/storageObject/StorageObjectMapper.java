package dev.eggl.adapter.persistence.jpa.storageObject;

import dev.eggl.domain.model.Category;
import dev.eggl.domain.model.StorageObject;

import java.util.List;

final class StorageObjectMapper {
    private StorageObjectMapper() {

    }

    static StorageObjectJpaEntity toJpaEntity(StorageObject entity) {
        StorageObjectJpaEntity jpaEntity = new StorageObjectJpaEntity();

        jpaEntity.setId(entity.getId());
        jpaEntity.setName(entity.getName());
        jpaEntity.setDescription(entity.getDescription());
        return jpaEntity;
    }

    static StorageObject toDomainEntity(StorageObjectJpaEntity jpaEntity) {
        return new StorageObject(jpaEntity.getId(), jpaEntity.getName(), jpaEntity.getDescription(), new Category(jpaEntity.getCategory().getId(), jpaEntity.getCategory().getName(), jpaEntity.getCategory().getDescription()));
    }

    static List<StorageObject> toDomainList(List<StorageObjectJpaEntity> jpaEntities) {
        return jpaEntities.stream().map(StorageObjectMapper::toDomainEntity).toList();
    }

}
