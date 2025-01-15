package dev.eggl.adapter.out.jpa.category;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class JpaCategoryPanacheRepository
        implements PanacheRepositoryBase<CategoryJpaEntity, Integer> {
}

