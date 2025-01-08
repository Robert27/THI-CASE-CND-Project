package dev.eggl.adapter.http.urlValidation;

import dev.eggl.port.out.UrlValidationPort;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Adapter for the URL validation service
 */
@ApplicationScoped
public class HttpUrlValidationController implements UrlValidationPort {

    private static final HttpClient client = HttpClient.newHttpClient();

    @Override
    public boolean validateUrl(String url) {
        try {
            URI targetUri = URI.create("http://localhost:2222/validate-url");
            HttpRequest request = HttpRequest.newBuilder(targetUri)
                    .POST(HttpRequest.BodyPublishers.ofString(url))
                    .header("Content-Type", "text/plain")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                String responseBody = response.body();
                return responseBody.contains("\"reachable\": true");
            } else {
                return false;
            }

        } catch (InterruptedException | IOException e) {
            throw new RuntimeException("Validation service unreachable", e);
        }
    }
}
