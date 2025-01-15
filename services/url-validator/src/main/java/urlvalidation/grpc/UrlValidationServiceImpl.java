package urlvalidation.grpc;

import io.grpc.stub.StreamObserver;
import jakarta.enterprise.context.ApplicationScoped;
import io.quarkus.grpc.GrpcService;
import io.grpc.stub.StreamObserver;
import jakarta.inject.Singleton;

import urlvalidation.Urlvalidation.*;
import urlvalidation.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@GrpcService
@Singleton
public class UrlValidationServiceImpl extends UrlValidationServiceGrpc.UrlValidationServiceImplBase {

    @Override
    public void validateUrl(ValidateUrlRequest request, StreamObserver<ValidateUrlResponse> responseObserver) {
        String url = request.getUrl();

        // (1) Check: null oder leer
        if (url.isEmpty()) {
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

            // (3) Erreichbarkeit prüfen (HEAD-Request)
            HttpURLConnection connection = (HttpURLConnection) testUrl.openConnection();
            connection.setRequestMethod("HEAD");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int responseCode = connection.getResponseCode();

            ValidateUrlResponse.Builder responseBuilder = ValidateUrlResponse.newBuilder()
                    .setValid(true); // Syntaktisch gültig

            if (responseCode >= 200 && responseCode < 400) {
                // Gültig + erreichbar
                responseBuilder.setReachable(true)
                        .setMessage("URL ist erreichbar (Response-Code: " + responseCode + ").");
            } else {
                // Gültig, aber nicht erreichbar
                responseBuilder.setReachable(false)
                        .setMessage("URL nicht erreichbar. Response-Code: " + responseCode + ".");
            }

            responseObserver.onNext(responseBuilder.build());
            responseObserver.onCompleted();

        } catch (MalformedURLException e) {
            // (4) Ungültige URL
            ValidateUrlResponse response = ValidateUrlResponse.newBuilder()
                    .setValid(false)
                    .setReachable(false)
                    .setMessage("Fehlerhafte URL: " + e.getMessage())
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (IOException e) {
            // (5) IOException => nicht erreichbar
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

