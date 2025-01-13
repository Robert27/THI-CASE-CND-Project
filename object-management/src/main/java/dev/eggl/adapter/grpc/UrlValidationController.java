package dev.eggl.adapter.grpc;

import dev.eggl.port.out.UrlValidationPort;
import dev.eggl.urlvalidation.UrlValidationService;
import dev.eggl.urlvalidation.UrlvalidationProto;
import io.quarkus.grpc.GrpcClient;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UrlValidationController implements UrlValidationPort {
    @GrpcClient("urlvalidation")
    UrlValidationService grpcUserService;


    @Override
    public boolean validateUrl(String url) {
        UrlvalidationProto.ValidateUrlRequest request = UrlvalidationProto.ValidateUrlRequest.newBuilder()
                .setUrl(url)
                .build();

        return grpcUserService.validateUrl(request)
                .onItem().transform(UrlvalidationProto.ValidateUrlResponse::getValid)
                .await().indefinitely();
    }
}
