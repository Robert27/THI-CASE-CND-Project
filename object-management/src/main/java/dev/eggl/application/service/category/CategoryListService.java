package dev.eggl.application.service.category;

import dev.eggl.domain.model.Category;
import dev.eggl.port.in.CategoryListPort;
import dev.eggl.port.out.CategoryPort;

import java.util.List;

public class CategoryListService implements CategoryListPort {
    private final CategoryPort categoryPort;

    public CategoryListService(CategoryPort categoryPort) {
        this.categoryPort = categoryPort;
    }

    @Override
    public List<Category> findAll() {
        return categoryPort.findAll();
    }
}
