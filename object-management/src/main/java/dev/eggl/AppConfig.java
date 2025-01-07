package dev.eggl;

import dev.eggl.application.service.ListCategoryService;
import dev.eggl.application.service.StorageObjectService;
import dev.eggl.port.in.AuthenticationUseCase;
import dev.eggl.port.in.CategoryListUseCase;
import dev.eggl.port.in.ListStorageObjectUseCase;
import dev.eggl.port.out.CategoryPort;
import dev.eggl.port.out.StorageObjectPort;
import dev.eggl.port.out.UrlValidationPort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;

@ApplicationScoped
public class AppConfig {


    @Inject
    Instance<AuthenticationUseCase> authenticationUseCase;

    @Inject
    Instance<CategoryPort> categoriesUseCase;

    @Inject
    Instance<StorageObjectPort> storageObjectPort;

    @Inject
    Instance<UrlValidationPort> urlValidationPort;

    @Produces
    @ApplicationScoped
    CategoryListUseCase findCategoriesUseCase() {
        return new ListCategoryService(categoriesUseCase.get());
    }


    @Produces
    @ApplicationScoped
    ListStorageObjectUseCase listStorageObjectUseCase() {
        return new StorageObjectService(storageObjectPort.get(), categoriesUseCase.get(), urlValidationPort.get(), authenticationUseCase.get());
    }
}
