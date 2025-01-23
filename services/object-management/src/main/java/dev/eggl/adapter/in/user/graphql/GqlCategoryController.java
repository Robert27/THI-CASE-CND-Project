package dev.eggl.adapter.in.user.graphql;

import dev.eggl.domain.model.Category;
import dev.eggl.port.in.CategoryListUseCase;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Query;

import java.util.List;

@GraphQLApi
public class GqlCategoryController {

    private final CategoryListUseCase categoryListUseCase;
    
    public GqlCategoryController(CategoryListUseCase categoryListUseCase) {
        this.categoryListUseCase = categoryListUseCase;
    }

    @Query("categories")
    public List<Category> getAllCategories() {
        return categoryListUseCase.findAll();
    }
}
