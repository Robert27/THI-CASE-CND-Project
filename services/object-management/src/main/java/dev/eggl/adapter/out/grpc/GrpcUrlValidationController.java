package dev.eggl.adapter.out.grpc;

import dev.eggl.port.out.UrlValidationPort;
import dev.eggl.urlvalidation.UrlValidationService;
import dev.eggl.urlvalidation.UrlvalidationProto;
import io.quarkus.grpc.GrpcClient;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GrpcUrlValidationController implements UrlValidationPort {
    @GrpcClient("urlvalidation")
    UrlValidationService grpcUserService;

    @Override
    public boolean validateUrl(String url) {
        UrlvalidationProto.ValidateUrlRequest request = UrlvalidationProto.ValidateUrlRequest.newBuilder()
                .setUrl(url)
                .build();
        boolean isValid = grpcUserService.validateUrl(request)
                .onItem().transform(response -> response.getValid() && response.getReachable())
                .await().indefinitely();
        return isValid;
    }
}