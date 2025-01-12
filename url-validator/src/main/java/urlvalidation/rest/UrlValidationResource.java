package urlvalidation.rest;

import urlvalidation.rest.dto.ValidationResult;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.core.Response;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@Path("/validate-url")
public class UrlValidationResource {

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response validateUrl(String url) {
        // (1) Check: null oder leer
        if (url == null || url.isEmpty()) {
            ValidationResult result = new ValidationResult(
                    false,
                    false,
                    "Keine gültige URL übergeben (leer oder null)."
            );
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(result)
                    .build();
        }

        try {
            // (2) Syntaktische Prüfung
            URL testUrl = new URL(url);

            // (3) Erreichbarkeit prüfen (HEAD-Request)
            HttpURLConnection connection = (HttpURLConnection) testUrl.openConnection();
            connection.setRequestMethod("HEAD");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int responseCode = connection.getResponseCode();

            if (responseCode >= 200 && responseCode < 400) {
                // Gültig + erreichbar
                ValidationResult result = new ValidationResult(
                        true,
                        true,
                        "URL ist erreichbar (Response-Code: " + responseCode + ")."
                );
                return Response.ok(result).build();
            } else {
                // Gültig, aber nicht erreichbar
                ValidationResult result = new ValidationResult(
                        true,
                        false,
                        "URL nicht erreichbar. Response-Code: " + responseCode + "."
                );
                return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                        .entity(result)
                        .build();
            }

        } catch (MalformedURLException e) {
            // (4) Ungültige URL
            ValidationResult result = new ValidationResult(
                    false,
                    false,
                    "Fehlerhafte URL: " + e.getMessage()
            );
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(result)
                    .build();
        } catch (IOException e) {
            // (5) IOException => nicht erreichbar
            ValidationResult result = new ValidationResult(
                    true,
                    false,
                    "URL nicht erreichbar: " + e.getMessage()
            );
            return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                    .entity(result)
                    .build();
        }
    }
}

