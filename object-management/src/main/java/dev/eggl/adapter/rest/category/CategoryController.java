package dev.eggl.adapter.rest.category;

import dev.eggl.domain.model.Category;
import dev.eggl.port.in.CategoryListPort;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/category")
@Produces(MediaType.APPLICATION_JSON)
public class CategoryController {

  private final CategoryListPort categoryListPort;

  public CategoryController(CategoryListPort categoryListPort) {
    this.categoryListPort = categoryListPort;
  }

  @GET
    public List<CategoryListModel> findAll() {
    List<Category> categories;

    try {
      categories = categoryListPort.findAll();
    } catch (Exception e) {
      throw new RuntimeException("Error while fetching categories", e);
    }

    return categories.stream()
            .map(CategoryListModel::fromDomainModel)
            .toList();
  }
}
