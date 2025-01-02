package dev.eggl.adapter.persistence.jpa;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class JpaCategoryPanacheRepository
        implements PanacheRepositoryBase<CategoryJpaEntity, String> {}

