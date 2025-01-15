package adapters.outbound.grpc;

import application.port.URLValidationPort;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import urlvalidation.UrlValidationServiceGrpc;
import urlvalidation.ValidateUrlRequest;
import urlvalidation.ValidateUrlResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class URLValidationGrpcClient implements URLValidationPort {

    private static final Logger LOGGER = LoggerFactory.getLogger(URLValidationGrpcClient.class);

    private UrlValidationServiceGrpc.UrlValidationServiceBlockingStub stub;

    @ConfigProperty(name = "urlvalidation.host")
    String host;

    @ConfigProperty(name = "urlvalidation.port")
    int port;

    @PostConstruct
    void init() {
        LOGGER.info("Initialisiere gRPC-Client für URLValidationService (Host: {}, Port: {})...", host, port);
        try {
            ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                    .usePlaintext() // Nur für lokale Tests ohne TLS
                    .build();
            this.stub = UrlValidationServiceGrpc.newBlockingStub(channel);
            LOGGER.info("gRPC-Client erfolgreich initialisiert.");
        } catch (Exception e) {
            LOGGER.error("Fehler beim Initialisieren des gRPC-Channels: {}", e.getMessage(), e);
            throw new IllegalStateException("gRPC-Client konnte nicht initialisiert werden.", e);
        }
    }

    @Override
    public boolean validateUrl(String url) {
        try {
            LOGGER.debug("Sende gRPC-Anfrage zur Validierung der URL: {}", url);

            // Baue die Anfrage
            ValidateUrlRequest request = ValidateUrlRequest.newBuilder()
                    .setUrl(url)
                    .build();

            // gRPC-Aufruf an den externen Service
            ValidateUrlResponse response = stub.validateUrl(request);

            boolean isValid = response.getValid() && response.getReachable();
            LOGGER.info("URL-Validierung abgeschlossen. Ergebnis: {} (Valid: {}, Reachable: {})",
                    isValid, response.getValid(), response.getReachable());

            return isValid;
        } catch (Exception e) {
            LOGGER.error("Fehler bei der Validierung der URL über gRPC: {}", e.getMessage(), e);
            throw new RuntimeException("Fehler bei der Validierung der URL.", e);
        }
    }
}