package dev.eggl.port.in;

import dev.eggl.domain.model.Category;

import java.util.List;

public interface CategoryListPort{
    List<Category> findAll();
}
