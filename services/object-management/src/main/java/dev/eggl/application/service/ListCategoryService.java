package dev.eggl.application.service;

import dev.eggl.domain.model.Category;
import dev.eggl.port.in.CategoryListUseCase;
import dev.eggl.port.out.CategoryRepository;

import java.util.List;

public class ListCategoryService implements CategoryListUseCase {
    private final CategoryRepository categoryRepository;

    public ListCategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

}
