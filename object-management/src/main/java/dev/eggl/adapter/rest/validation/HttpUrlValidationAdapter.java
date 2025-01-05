package dev.eggl.adapter.rest.validation;

import dev.eggl.port.out.UrlValidationPort;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@ApplicationScoped
public class HttpUrlValidationAdapter implements UrlValidationPort {

    private static final HttpClient client = HttpClient.newHttpClient();

    @Override
    public boolean validateUrl(String url) {
        try {
            URI targetUri = URI.create("http://localhost:2222/validate-url"); // Update with the correct URL to your service
            HttpRequest request = HttpRequest.newBuilder(targetUri)
                    .POST(HttpRequest.BodyPublishers.ofString(url))
                    .header("Content-Type", "text/plain")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                // Parse the response to check if the URL is valid and reachable
                // Assuming the response body contains a JSON with a "valid" and "reachable" field
                String responseBody = response.body();
                // Here you could use a JSON parser (e.g., Jackson or Gson) to parse the response.
                // For now, let's assume the status is "true" if the URL is reachable:
                return responseBody.contains("\"reachable\": true");
            } else {
                return false; // Service responded with an error
            }

        } catch (InterruptedException | IOException e) {
            throw new RuntimeException("Validation service unreachable", e);
        }
    }
}
