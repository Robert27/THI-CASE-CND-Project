package orderlistservice.config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@ApplicationScoped
public class ExecutorConfig {

    @Produces
    @Named("myExecutorService")
    public ExecutorService produceExecutorService() {
        // Beispiel: Threadpool mit fester Größe
        return Executors.newFixedThreadPool(10);
    }
}
