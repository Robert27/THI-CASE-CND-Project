package dev.eggl.adapter.rest.category;

import dev.eggl.domain.model.Category;
import dev.eggl.port.in.CategoryListUseCase;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/category")
@Produces(MediaType.APPLICATION_JSON)
public class CategoryController {

    private final CategoryListUseCase categoryListUseCase;

    public CategoryController(CategoryListUseCase categoryListUseCase) {
        this.categoryListUseCase = categoryListUseCase;
    }

    @GET
    public List<ListCategoryModel> findAll() {
        List<Category> categories;

        try {
            categories = categoryListUseCase.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error while fetching categories", e);
        }

        return categories.stream()
                .map(ListCategoryModel::fromDomainModel)
                .toList();
    }

}
