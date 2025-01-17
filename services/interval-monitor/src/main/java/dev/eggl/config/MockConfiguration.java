package dev.eggl.config;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import java.time.LocalDate;

@Getter
@ApplicationScoped
public class MockConfiguration {
    @ConfigProperty(name = "mock.enabled", defaultValue = "false")
    boolean mockEnabled;

    @Setter
    private LocalDate mockDate = LocalDate.now();

}
