package urlvalidation.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;

public class UrlValidationServer {
    public static void main(String[] args) throws Exception {
        // Erstelle den Server und registriere den Service
        Server server = ServerBuilder.forPort(50051)
                .addService(new UrlValidationServiceImpl())
                .build();

        System.out.println("gRPC Server gestartet auf Port 50051...");
        server.start();
        server.awaitTermination();
    }
}
