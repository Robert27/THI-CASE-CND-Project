package dev.eggl.adapter.persistence.jpa.storageObject;

import dev.eggl.domain.model.StorageObject;
import dev.eggl.port.out.StorageObjectRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class JpaStorageObjectRepository implements StorageObjectRepository {
    private final JpaStorageObjectPanacheRepository panacheRepository;


    public JpaStorageObjectRepository() {
        this.panacheRepository = new JpaStorageObjectPanacheRepository();
    }

    @Override
    public StorageObject save(StorageObject storageObject) {
        StorageObjectJpaEntity storageObjectJpaEntity = StorageObjectMapper.toJpaEntity(storageObject);
        panacheRepository.persist(storageObjectJpaEntity);
        return StorageObjectMapper.toDomainEntity(storageObjectJpaEntity);
    }

    @Override
    public List<StorageObject> findAll(Integer userId) {
        List<StorageObjectJpaEntity> storageObjectJpaEntities = panacheRepository.find("userId", userId).list();
        return StorageObjectMapper.toDomainList(storageObjectJpaEntities);
    }

    @Override
    public StorageObject findById(Integer id, Integer userId) {
        StorageObjectJpaEntity storageObjectJpaEntity = panacheRepository.find("id = ?1 and userId = ?2", id).firstResult();
        if (storageObjectJpaEntity == null) {
            throw new IllegalArgumentException("Storage object not found");
        }
        return StorageObjectMapper.toDomainEntity(storageObjectJpaEntity);
    }

    @Override
    public List<StorageObject> findByIds(List<Integer> ids) {
        return StorageObjectMapper.toDomainList(panacheRepository.find("id in ?1", ids).list());
    }

    @Override
    public Boolean existsByUrl(String reorderUrl, Integer userId) {
        return panacheRepository.count("reorderUrl = ?1 and userId = ?2", reorderUrl, userId) > 0;
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

    @Override
    public StorageObject delete(Integer id, Integer userId) {
        StorageObjectJpaEntity storageObjectJpaEntity = panacheRepository.find("id = ?1 and userId = ?2", id, userId).firstResult();
        if (storageObjectJpaEntity == null) {
            throw new IllegalArgumentException("Storage object not found");
        }
        panacheRepository.delete(storageObjectJpaEntity);
        return StorageObjectMapper.toDomainEntity(storageObjectJpaEntity);
    }

    @Override
    public boolean existsByNameAndCategory(String name, Integer categoryId, Integer userId) {
        return panacheRepository.count("name = ?1 and categoryId = ?2 and userId = ?3", name, categoryId, userId) > 0;
    }

    @Override
    public List<StorageObject> findByWeekDay(Integer weekDay, Integer userId) {
        return StorageObjectMapper.toDomainList(panacheRepository.find("weekDay = ?1 and userId = ?2", weekDay, userId).list());
    }

}
