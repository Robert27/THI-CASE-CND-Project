package dev.eggl.config;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import java.time.LocalDate;

@ApplicationScoped
public class MockConfiguration {
    @ConfigProperty(name = "mock.enabled", defaultValue = "false")
    private boolean mockEnabled;

    private LocalDate mockDate = LocalDate.now();

    public boolean isMockEnabled() {
        return mockEnabled;
    }

    public LocalDate getMockDate() {
        return mockDate;
    }

    public void setMockDate(LocalDate mockDate) {
        this.mockDate = mockDate;
    }
}