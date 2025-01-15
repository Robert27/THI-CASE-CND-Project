package adapters.outbound.rest.pricecheck;

import adapters.outbound.rest.pricecheck.dto.PriceResponse;
import application.port.PriceCheckPort;
import domain.model.PriceLog;
import domain.ItemPriceService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDateTime;

/**
 * Beispiel: Ruft einen Mock-Server ab, der ein JSON mit id, price und available zurückgibt.
 */
@ApplicationScoped
public class PriceCheckAdapter implements PriceCheckPort {


    @Override
    public PriceLog checkPrice(Integer itemId, String url) {

        // Standard-JAX-RS-Client erzeugen
        try (Client client = ClientBuilder.newClient()) {
            // "url"  aufrufen:
            WebTarget target = client.target(url);

            // GET-Request absetzen und JSON als Antwort erwarten
            Response response = target
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            // HTTP-Status checken
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {

                // JSON in PriceResponse parsen
                PriceResponse priceResponse = response.readEntity(PriceResponse.class);

                // Aus den Feldern von PriceResponse einen PriceLog bauen
                // und den aktuellen Zeitstempel setzen
                return new PriceLog(
                        priceResponse.getId(),
                        priceResponse.getPrice(),
                        priceResponse.getAvailable(),
                        LocalDateTime.now()
                );
            } else {
                // Fallback, falls der Mock nicht 200 liefert
                System.err.println("Unerwarteter HTTP-Status: " + response.getStatus());
                return new PriceLog(itemId, 0.0, 0, LocalDateTime.now());
            }
        } catch (Exception e) {
            // Bei Fehlern: Loggen und Fallback-Werte zurückgeben
            System.err.println("Fehler beim Aufruf: " + e.getMessage());
            return new PriceLog(itemId, 0.0, 0, LocalDateTime.now());
        }
    }
}