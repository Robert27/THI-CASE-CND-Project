package dev.eggl.mock;

import dev.eggl.config.MockConfiguration;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class MockControllerTest {

    @Inject
    MockConfiguration mockConfig;

    @BeforeEach
    void resetMockDate() {
        mockConfig.setMockDate(LocalDate.now());
    }

    @Test
    void testDefaultMockDate() {
        LocalDate date = mockConfig.getMockDate();
        assertEquals(LocalDate.now(), date);
    }

    @Test
    void testSetMockDate() {
        LocalDate testDate = LocalDate.of(2024, 1, 18);
        mockConfig.setMockDate(testDate);
        assertEquals(testDate, mockConfig.getMockDate());
    }

    @Test
    void testMockEnabledEndpoint() {
        given()
                .when().get("/mock/enabled")
                .then()
                .statusCode(200)
                .body(equalTo("false")); // Default value from @ConfigProperty
    }

    @Test
    void testSetMockDateEndpoint() {
        given()
                .body("2024-01-18")
                .contentType("text/plain")
                .when().post("/mock/date")
                .then()
                .statusCode(204);

        assertEquals(LocalDate.of(2024, 1, 18), mockConfig.getMockDate());
    }
}
