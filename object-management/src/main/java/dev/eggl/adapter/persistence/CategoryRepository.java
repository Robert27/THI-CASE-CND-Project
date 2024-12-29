package dev.eggl.adapter.persistence;

import dev.eggl.domain.model.Category;
import dev.eggl.ports.CategoryPort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.beans.Transient;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class CategoryRepository implements CategoryPort {

    @Override
    public Category findById(Long id) {
        return CategoryEntity.findByIdOptional(id)
                .map(entity -> CategoryEntityMapper.toDomain((CategoryEntity) entity)) // Explicit cast
                .orElse(null);
    }

    @Override
    public List<Category> findAll() {
        return CategoryEntity.listAll().stream()
                .map(entity -> CategoryEntityMapper.toDomain((CategoryEntity) entity))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void save(Category category) {
        CategoryEntity entity = CategoryEntityMapper.toEntity(category);
        entity.persist();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        CategoryEntity.deleteById(id);
    }
}