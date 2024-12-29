package dev.eggl.adapter.persistence;

import dev.eggl.domain.model.Category;

public class CategoryEntityMapper {

    public static Category toDomain(CategoryEntity entity) {
        return new Category(entity.id, entity.getName(), entity.getDescription());
    }

    public static CategoryEntity toEntity(Category domain) {
        CategoryEntity entity = new CategoryEntity();
        entity.id = domain.getId();
        entity.setName(domain.getName());
        entity.setDescription(domain.getDescription());
        return entity;
    }
}
