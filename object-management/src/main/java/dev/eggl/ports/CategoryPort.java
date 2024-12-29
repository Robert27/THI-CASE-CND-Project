package dev.eggl.ports;

import dev.eggl.domain.model.Category;

import java.util.List;

public interface CategoryPort {
    Category findById(Long id);
    List<Category> findAll();
    void save(Category category);
    void delete(Long id);
}
