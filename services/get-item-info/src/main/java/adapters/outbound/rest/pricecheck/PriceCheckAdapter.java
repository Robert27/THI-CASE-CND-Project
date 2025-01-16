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
import io.smallrye.common.annotation.Blocking;

import java.time.LocalDateTime;

/**
 * Ruft einen Mock-Server ab, der ein JSON mit id, price und available zurückgibt.
 */
@ApplicationScoped
@Blocking
public class PriceCheckAdapter implements PriceCheckPort {


    @Override
    public PriceLog checkPrice(Integer itemId, String url) {
        try (Client client = ClientBuilder.newClient()) {
            WebTarget target = client.target(url);

            // GET-Request absetzen und JSON als Antwort erwarten
            Response response = target
                    .request(MediaType.APPLICATION_JSON)
                    .get();

            // HTTP-Status checken
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                // JSON in PriceResponse parsen
                PriceResponse priceResponse = response.readEntity(PriceResponse.class);

                // Aus priceResponse einen PriceLog bauen (itemId übernehmen)
                return new PriceLog(
                        itemId,                           // <-- itemId direkt setzen
                        priceResponse.getPrice(),
                        priceResponse.getAvailable(),
                        LocalDateTime.now()
                );
            } else {
                // Unerwarteter HTTP-Status -> Fehler werfen
                throw new IllegalStateException("Unerwarteter HTTP-Status: " + response.getStatus());
            }

        } catch (Exception e) {
            e.printStackTrace();

            // Bei Fehlern -> Exception werfen, damit kein leerer Log gespeichert wird
            throw new RuntimeException("Fehler beim Aufruf: " + e.getMessage(), e);
        }
    }
}