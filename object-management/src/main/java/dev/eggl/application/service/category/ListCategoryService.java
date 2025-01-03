package dev.eggl.application.service.category;

import dev.eggl.domain.model.Category;
import dev.eggl.port.in.category.CategoryListUseCase;
import dev.eggl.port.out.CategoryPort;

import java.util.List;

public class ListCategoryService implements CategoryListUseCase {
    private final CategoryPort categoryPort;

    public ListCategoryService(CategoryPort categoryPort) {
        this.categoryPort = categoryPort;
    }

    @Override
    public List<Category> findAll() {
        return categoryPort.findAll();
    }
}
