package dev.eggl.adapter.in.rest;

import dev.eggl.domain.model.Category;

public record ListCategoryModel(Integer id, String name, String description) {
    public static ListCategoryModel fromDomainModel(Category category) {
        return new ListCategoryModel(category.getId(), category.getName(), category.getDescription());
    }
}
