package dev.eggl.port.in;

import dev.eggl.domain.model.Category;

import java.util.List;

public interface CategoryListUseCase {
    List<Category> findAll();
}
