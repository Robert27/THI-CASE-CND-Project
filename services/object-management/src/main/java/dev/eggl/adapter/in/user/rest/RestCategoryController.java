package dev.eggl.adapter.in.user.rest;

import dev.eggl.adapter.in.user.dto.category.ListCategoryResponse;
import dev.eggl.domain.model.Category;
import dev.eggl.port.in.CategoryListUseCase;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/category")
@Produces(MediaType.APPLICATION_JSON)
public class RestCategoryController {

    private final CategoryListUseCase categoryListUseCase;

    public RestCategoryController(CategoryListUseCase categoryListUseCase) {
        this.categoryListUseCase = categoryListUseCase;
    }

    @GET
    public List<ListCategoryResponse> findAll() {
        List<Category> categories;

        try {
            categories = categoryListUseCase.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error while fetching categories", e);
        }

        return categories.stream()
                .map(ListCategoryResponse::fromDomainModel)
                .toList();
    }

}
