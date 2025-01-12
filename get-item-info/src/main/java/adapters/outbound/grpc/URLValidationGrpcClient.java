package adapters.outbound.grpc;

import application.port.URLValidationPort;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import urlvalidation.UrlValidationServiceGrpc;
import urlvalidation.ValidateUrlRequest;
import urlvalidation.ValidateUrlResponse;

@ApplicationScoped
public class URLValidationGrpcClient implements URLValidationPort {

    private final UrlValidationServiceGrpc.UrlValidationServiceBlockingStub stub;

    // Injektion der Konfigurationswerte
    @ConfigProperty(name = "urlvalidation.host")
    String host;

    @ConfigProperty(name = "urlvalidation.port")
    int port;

    public URLValidationGrpcClient() {
        // Erstelle den gRPC-Channel
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        this.stub = UrlValidationServiceGrpc.newBlockingStub(channel);
    }

    @Override
    public boolean validateUrl(String url) {
        ValidateUrlRequest request = ValidateUrlRequest.newBuilder().setUrl(url).build();
        ValidateUrlResponse response = stub.validateUrl(request);
        return response.getValid() && response.getReachable();
    }
}
