package dev.eggl.adapter.in.user.dto.category;

import dev.eggl.domain.model.Category;

public record ListCategoryResponse(Integer id, String name, String description) {
    public static ListCategoryResponse fromDomainModel(Category category) {
        return new ListCategoryResponse(category.getId(), category.getName(), category.getDescription());
    }
}
