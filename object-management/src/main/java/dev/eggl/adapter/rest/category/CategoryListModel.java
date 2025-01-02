package dev.eggl.adapter.rest.category;

import dev.eggl.domain.model.Category;

public record CategoryListModel(Integer id, String name) {
    public static CategoryListModel fromDomainModel(Category category) {
        return new CategoryListModel(category.getId(), category.getName());
    }
}
