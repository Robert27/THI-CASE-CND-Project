package dev.eggl.adapter.out.jpa.storageObject;

import dev.eggl.domain.model.StorageObject;
import dev.eggl.port.out.StorageObjectRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Map;

@ApplicationScoped
public class JpaStorageObjectRepositoryImpl implements StorageObjectRepository {
    private final JpaStorageObjectPanacheRepository panacheRepository;

    public JpaStorageObjectRepositoryImpl() {
        this.panacheRepository = new JpaStorageObjectPanacheRepository();
    }

    @Transactional
    @Override
    public StorageObject save(StorageObject storageObject) {
        StorageObjectJpaEntity storageObjectJpaEntity = StorageObjectMapper.toJpaEntity(storageObject);
        panacheRepository.persist(storageObjectJpaEntity);
        System.out.println("Successfully created storage object with id: " + storageObjectJpaEntity.getId());
        return StorageObjectMapper.toDomainEntity(storageObjectJpaEntity);
    }

    @Override
    public List<StorageObject> findAll(Integer userId) {
        List<StorageObjectJpaEntity> storageObjectJpaEntities = panacheRepository.find("userId", userId).list();
        return StorageObjectMapper.toDomainList(storageObjectJpaEntities);
    }

    @Override
    public StorageObject findById(Integer id, Integer userId) {
        StorageObjectJpaEntity storageObjectJpaEntity = panacheRepository.find("id = ?1 and userId = ?2", id, userId)
                .firstResult();
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
    public Map<Integer, List<Integer>> findAllDayUsers(Integer weekDay, List<Integer> userIds) {
        List<StorageObjectJpaEntity> results = panacheRepository.find(
                "weekday = ?1 and userId in ?2", weekDay, userIds).list();

        return results.stream().collect(
                java.util.stream.Collectors.groupingBy(StorageObjectJpaEntity::getUserId,
                        java.util.stream.Collectors.mapping(StorageObjectJpaEntity::getId,
                                java.util.stream.Collectors.toList())));
    }

    @Override
    public Boolean existsByUrl(String reorderUrl, Integer userId) {
        return panacheRepository.count("reorderUrl = ?1 and userId = ?2", reorderUrl, userId) > 0;
    }

    @Override
    public StorageObject update(StorageObject storageObject) {
        StorageObjectJpaEntity storageObjectJpaEntity = StorageObjectMapper.toJpaEntity(storageObject);
        StorageObjectJpaEntity updatedStorageObjectJpaEntity = panacheRepository
                .findById(String.valueOf(storageObject.getId()));
        if (updatedStorageObjectJpaEntity == null) {
            throw new IllegalArgumentException("Storage object not found");
        }
        updatedStorageObjectJpaEntity.setName(storageObjectJpaEntity.getName());
        updatedStorageObjectJpaEntity.setDescription(storageObjectJpaEntity.getDescription());
        updatedStorageObjectJpaEntity.setCategoryId(storageObjectJpaEntity.getCategoryId());
        updatedStorageObjectJpaEntity.setReorderUrl(storageObjectJpaEntity.getReorderUrl());
        updatedStorageObjectJpaEntity.setQuantity(storageObjectJpaEntity.getQuantity());
        updatedStorageObjectJpaEntity.setWeekday(storageObjectJpaEntity.getWeekday());
        panacheRepository.persist(updatedStorageObjectJpaEntity);
        System.out.println("Successfully updated storage object with id: " + updatedStorageObjectJpaEntity.getId());
        return StorageObjectMapper.toDomainEntity(storageObjectJpaEntity);
    }

    @Override
    public StorageObject delete(Integer id, Integer userId) {
        StorageObjectJpaEntity storageObjectJpaEntity = panacheRepository.find("id = ?1 and userId = ?2", id, userId)
                .firstResult();
        if (storageObjectJpaEntity == null) {
            throw new IllegalArgumentException("Storage object not found");
        }
        panacheRepository.delete(storageObjectJpaEntity);
        System.out.println("Successfully deleted storage object with id: " + storageObjectJpaEntity.getId());
        return StorageObjectMapper.toDomainEntity(storageObjectJpaEntity);
    }

    @Override
    public boolean existsByNameAndCategory(String name, Integer categoryId, Integer userId) {
        return panacheRepository.count("name = ?1 and categoryId = ?2 and userId = ?3", name, categoryId, userId) > 0;
    }

}
