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
    public void save(StorageObject storageObject) {
        panacheRepository.getEntityManager().merge(StorageObjectMapper.toJpaEntity(storageObject));
    }

    @Override
    public List<StorageObject> findAll() {
        List<StorageObjectJpaEntity> storageObjectJpaEntities = panacheRepository.findAll().list();
        return StorageObjectMapper.toDomainList(storageObjectJpaEntities);
    }

}
