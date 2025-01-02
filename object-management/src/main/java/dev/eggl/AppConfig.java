package dev.eggl;

import dev.eggl.application.service.category.CategoryListService;
import dev.eggl.port.in.CategoryListPort;
import dev.eggl.port.out.CategoryPort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;

public class AppConfig {
    @Inject
    Instance<CategoryPort> categoryPort;

    @Produces
    @ApplicationScoped
    CategoryListPort findCategoriesUseCase() {
        return new CategoryListService(categoryPort.get());
    }
}