package dev.eggl;

import dev.eggl.application.service.InternalStorageObjectServiceImpl;
import dev.eggl.application.service.ListCategoryServiceImpl;
import dev.eggl.application.service.StorageObjectServiceImpl;
import dev.eggl.port.in.InternalStorageObjectUseCase;
import dev.eggl.port.in.StorageObjectUseCase;
import dev.eggl.port.out.AuthenticationUseCase;
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
    ListCategoryServiceImpl listCategoryService() {
        return new ListCategoryServiceImpl(categoriesUseCase.get());
    }

    @Produces
    @ApplicationScoped
    InternalStorageObjectUseCase internalStorageObjectUseCase() {
        return new InternalStorageObjectServiceImpl(storageObjectPort.get()) {
        };
    }

    @Produces
    @ApplicationScoped
    StorageObjectUseCase listStorageObjectUseCase() {
        return new StorageObjectServiceImpl(storageObjectPort.get(), categoriesUseCase.get(), urlValidationPort.get(), authenticationUseCase.get());
    }
}
