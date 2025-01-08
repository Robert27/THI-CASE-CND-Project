package dev.eggl;

import dev.eggl.application.service.ListCategoryService;
import dev.eggl.application.service.StorageObjectService;
import dev.eggl.port.in.AuthenticationUseCase;
import dev.eggl.port.in.ListStorageObjectUseCase;
import dev.eggl.port.out.CategoryRepository;
import dev.eggl.port.out.StorageObjectRepository;
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
    Instance<CategoryRepository> categoriesUseCase;

    @Inject
    Instance<StorageObjectRepository> storageObjectPort;

    @Inject
    Instance<UrlValidationPort> urlValidationPort;

    @Produces
    @ApplicationScoped
    ListCategoryService listCategoryService() {
        return new ListCategoryService(categoriesUseCase.get());
    }

    @Produces
    @ApplicationScoped
    ListStorageObjectUseCase listStorageObjectUseCase() {
        return new StorageObjectService(storageObjectPort.get(), categoriesUseCase.get(), urlValidationPort.get(), authenticationUseCase.get());
    }
}
