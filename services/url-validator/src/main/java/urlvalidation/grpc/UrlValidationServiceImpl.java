package urlvalidation.grpc;

import io.grpc.stub.StreamObserver;
import io.quarkus.grpc.GrpcService;
import jakarta.inject.Singleton;

import urlvalidation.Urlvalidation.*;
import urlvalidation.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

@GrpcService
@Singleton
public class UrlValidationServiceImpl extends UrlValidationServiceGrpc.UrlValidationServiceImplBase {

    private static final Logger LOGGER = Logger.getLogger(UrlValidationServiceImpl.class.getName());

    @Override
    public void validateUrl(ValidateUrlRequest request, StreamObserver<ValidateUrlResponse> responseObserver) {
        String url = request.getUrl();

        LOGGER.info("Received URL validation request for: " + url);

        // (1) Check: null oder leer
        if (url.isEmpty()) {
            LOGGER.warning("Validation failed: Empty or null URL.");
            ValidateUrlResponse response = ValidateUrlResponse.newBuilder()
                    .setValid(false)
                    .setReachable(false)
                    .setMessage("Keine gültige URL übergeben (leer oder null).")
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            return;
        }

        try {
            // (2) Syntaktische Prüfung
            URL testUrl = new URL(url);
            LOGGER.info("URL syntax is valid: " + url);

            // (3) Erreichbarkeit prüfen (HEAD-Request)
            HttpURLConnection connection = (HttpURLConnection) testUrl.openConnection();
            connection.setRequestMethod("HEAD");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int responseCode = connection.getResponseCode();
            LOGGER.info("Received response code: " + responseCode + " for URL: " + url);

            ValidateUrlResponse.Builder responseBuilder = ValidateUrlResponse.newBuilder()
                    .setValid(true); // Syntaktisch gültig

            if (responseCode >= 200 && responseCode < 400) {
                // Gültig + erreichbar
                LOGGER.info("URL is reachable: " + url);
                responseBuilder.setReachable(true)
                        .setMessage("URL ist erreichbar (Response-Code: " + responseCode + ").");
            } else {
                // Gültig, aber nicht erreichbar
                LOGGER.warning("URL is not reachable. Response-Code: " + responseCode + " for URL: " + url);
                responseBuilder.setReachable(false)
                        .setMessage("URL nicht erreichbar. Response-Code: " + responseCode + ".");
            }

            responseObserver.onNext(responseBuilder.build());
            responseObserver.onCompleted();

        } catch (MalformedURLException e) {
            // (4) Ungültige URL
            LOGGER.log(Level.SEVERE, "Malformed URL: " + url);
            ValidateUrlResponse response = ValidateUrlResponse.newBuilder()
                    .setValid(false)
                    .setReachable(false)
                    .setMessage("Fehlerhafte URL: " + e.getMessage())
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (IOException e) {
            // (5) IOException => nicht erreichbar
            LOGGER.log(Level.SEVERE, "URL not reachable: " + url);
            ValidateUrlResponse response = ValidateUrlResponse.newBuilder()
                    .setValid(true)
                    .setReachable(false)
                    .setMessage("URL nicht erreichbar: " + e.getMessage())
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }
}