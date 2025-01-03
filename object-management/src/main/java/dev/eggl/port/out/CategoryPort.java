package dev.eggl.port.out;

import dev.eggl.domain.model.Category;

import java.util.List;

public interface CategoryPort {
    List<Category> findAll();
    Boolean existsById(Integer id);
}
