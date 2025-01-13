package urlvalidation.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import java.util.ResourceBundle;

public class UrlValidationServer {
    public static void main(String[] args) throws Exception {
        // Lese den Port aus der Umgebungsvariable oder nutze den Standardport 50051
        ResourceBundle bundle = ResourceBundle.getBundle("application");
        int port = Integer.parseInt(bundle.getString("grpc.server.port"));

        // Erstelle den Server und registriere den Service
        Server server = ServerBuilder.forPort(port)
                .addService(new UrlValidationServiceImpl())
                .build();

        System.out.println("gRPC Server gestartet auf Port " + port + "...");
        server.start();
        server.awaitTermination();
    }
}
