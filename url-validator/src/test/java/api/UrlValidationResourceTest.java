package api;
import io.grpc.testing.GrpcCleanupRule;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.ManagedChannel;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import urlvalidation.UrlValidationServiceGrpc;
import urlvalidation.Urlvalidation.ValidateUrlRequest;
import urlvalidation.Urlvalidation.ValidateUrlResponse;
import urlvalidation.grpc.UrlValidationServiceImpl;

import static org.junit.jupiter.api.Assertions.*;
// Tests for REST resource
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;

class UrlValidationServiceImplTest {

    @Rule
    public final GrpcCleanupRule grpcCleanup = new GrpcCleanupRule();

    @Test
    void validateUrl_validAndReachable() throws Exception {
        // Set up the server and client
        String serverName = InProcessServerBuilder.generateName();
        grpcCleanup.register(
                InProcessServerBuilder
                        .forName(serverName)
                        .directExecutor()
                        .addService(new UrlValidationServiceImpl())
                        .build()
                        .start());

        ManagedChannel channel = grpcCleanup.register(
                InProcessChannelBuilder.forName(serverName).directExecutor().build());

        UrlValidationServiceGrpc.UrlValidationServiceBlockingStub stub = UrlValidationServiceGrpc.newBlockingStub(channel);

        // Mock a reachable URL
        ValidateUrlRequest request = ValidateUrlRequest.newBuilder().setUrl("http://www.google.com").build();

        ValidateUrlResponse response = stub.validateUrl(request);

        assertNotNull(response);
        assertTrue(response.getValid());
        assertTrue(response.getReachable());
        assertTrue(response.getMessage().contains("URL ist erreichbar"));
    }

    @Test
    void validateUrl_invalidUrl() throws Exception {
        // Set up the server and client
        String serverName = InProcessServerBuilder.generateName();
        grpcCleanup.register(
                InProcessServerBuilder
                        .forName(serverName)
                        .directExecutor()
                        .addService(new UrlValidationServiceImpl())
                        .build()
                        .start());

        ManagedChannel channel = grpcCleanup.register(
                InProcessChannelBuilder.forName(serverName).directExecutor().build());

        UrlValidationServiceGrpc.UrlValidationServiceBlockingStub stub = UrlValidationServiceGrpc.newBlockingStub(channel);

        // Mock an invalid URL
        ValidateUrlRequest request = ValidateUrlRequest.newBuilder().setUrl("invalid-url").build();

        ValidateUrlResponse response = stub.validateUrl(request);

        assertNotNull(response);
        assertFalse(response.getValid());
        assertFalse(response.getReachable());
        assertTrue(response.getMessage().contains("Fehlerhafte URL"));
    }
}


@QuarkusTest
class UrlValidationResourceTest {

    @Test
    void validateUrl_validAndReachable() {
        given()
                .contentType("text/plain")
                .body("http://www.google.com")
                .when()
                .post("/validate-url")
                .then()
                .statusCode(200)
                .body("valid", is(true))
                .body("reachable", is(true))
                .body("message", containsString("URL ist erreichbar"));
    }

    @Test
    void validateUrl_invalidUrl() {
        given()
                .contentType("text/plain")
                .body("invalid-url")
                .when()
                .post("/validate-url")
                .then()
                .statusCode(400)
                .body("valid", is(false))
                .body("reachable", is(false))
                .body("message", containsString("Fehlerhafte URL"));
    }

    @Test
    void validateUrl_emptyUrl() {
        given()
                .contentType("text/plain")
                .body("")
                .when()
                .post("/validate-url")
                .then()
                .statusCode(400)
                .body("valid", is(false))
                .body("reachable", is(false))
                .body("message", containsString("Keine gültige URL übergeben"));
    }
}
