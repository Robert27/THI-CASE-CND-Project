package dev.eggl;

import dev.eggl.application.service.category.ListCategoryService;
import dev.eggl.application.service.category.ListStorageObjectService;
import dev.eggl.port.in.category.CategoryListUseCase;
import dev.eggl.port.in.storageObject.ListStorageObjectUseCase;
import dev.eggl.port.out.CategoryPort;
import dev.eggl.port.out.StorageObjectPort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;

public class AppConfig {
    @Inject
    Instance<CategoryPort> categoryPort;

    @Inject
    Instance<StorageObjectPort> storageObjectPort;

    @Produces
    @ApplicationScoped
    CategoryListUseCase findCategoriesUseCase() {
        return new ListCategoryService(categoryPort.get());
    }

    @Produces
    @ApplicationScoped
    ListStorageObjectUseCase listStorageObjectUseCase() {
        return new ListStorageObjectService(storageObjectPort.get());
    }
}
