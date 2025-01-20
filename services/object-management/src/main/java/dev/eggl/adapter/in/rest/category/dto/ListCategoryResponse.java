package dev.eggl.adapter.in.rest.category.dto;

import dev.eggl.domain.model.Category;

public record ListCategoryResponse(Integer id, String name, String description) {
    public static ListCategoryResponse fromDomainModel(Category category) {
        return new ListCategoryResponse(category.getId(), category.getName(), category.getDescription());
    }
}
