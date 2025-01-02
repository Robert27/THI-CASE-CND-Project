package dev.eggl.adapter.persistence.jpa;

import dev.eggl.domain.model.Category;

import java.util.List;

final class CategoryMapper {

    private CategoryMapper() {
    }

    static CategoryJpaEntity toJpaEntity(Category entity) {
       CategoryJpaEntity jpaEntity = new CategoryJpaEntity();

         jpaEntity.setId(entity.getId());
            jpaEntity.setName(entity.getName());
            jpaEntity.setDescription(entity.getDescription());

            return jpaEntity;
    }

    static Category toDomainEntity(CategoryJpaEntity jpaEntity) {
        return new Category(jpaEntity.getId(), jpaEntity.getName(), jpaEntity.getDescription());
    }

    static List<Category> toDomainList(List<CategoryJpaEntity> jpaEntities) {
        return jpaEntities.stream().map(CategoryMapper::toDomainEntity).toList();
    }
}
