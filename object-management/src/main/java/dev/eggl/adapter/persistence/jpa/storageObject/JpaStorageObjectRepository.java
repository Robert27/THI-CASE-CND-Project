package dev.eggl.adapter.persistence.jpa.storageObject;

import dev.eggl.domain.model.StorageObject;
import dev.eggl.port.out.StorageObjectPort;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class JpaStorageObjectRepository implements StorageObjectPort {
    private final JpaStorageObjectPanacheRepository panacheRepository;

    public JpaStorageObjectRepository(JpaStorageObjectPanacheRepository panacheRepository) {
        this.panacheRepository = panacheRepository;

    }

    @Override
    public StorageObject save(StorageObject storageObject) {
        StorageObjectJpaEntity storageObjectJpaEntity = StorageObjectMapper.toJpaEntity(storageObject);
        panacheRepository.persist(storageObjectJpaEntity);
        return StorageObjectMapper.toDomainEntity(storageObjectJpaEntity);
    }

    @Override
    public List<StorageObject> findAll() {
        List<StorageObjectJpaEntity> storageObjectJpaEntities = panacheRepository.findAll().list();
        return StorageObjectMapper.toDomainList(storageObjectJpaEntities);
    }

    @Override
    public StorageObject update(StorageObject storageObject) {
        StorageObjectJpaEntity storageObjectJpaEntity = StorageObjectMapper.toJpaEntity(storageObject);
        StorageObjectJpaEntity updatedStorageObjectJpaEntity = panacheRepository.findById(String.valueOf(storageObject.getId()));
        if (updatedStorageObjectJpaEntity == null) {
            throw new IllegalArgumentException("Storage object not found");
        }
        updatedStorageObjectJpaEntity.setName(storageObjectJpaEntity.getName());
        updatedStorageObjectJpaEntity.setDescription(storageObjectJpaEntity.getDescription());
        updatedStorageObjectJpaEntity.setCategoryId(storageObjectJpaEntity.getCategoryId());
        updatedStorageObjectJpaEntity.setReorderUrl(storageObjectJpaEntity.getReorderUrl());
        // TODO: Update the updated timestamp
        panacheRepository.persist(updatedStorageObjectJpaEntity);
        return StorageObjectMapper.toDomainEntity(storageObjectJpaEntity);
    }

}
