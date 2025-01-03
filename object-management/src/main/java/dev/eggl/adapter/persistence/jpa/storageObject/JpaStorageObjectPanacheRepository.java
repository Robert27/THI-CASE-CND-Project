package dev.eggl.adapter.persistence.jpa.storageObject;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class JpaStorageObjectPanacheRepository implements PanacheRepositoryBase<StorageObjectJpaEntity, String> {}
