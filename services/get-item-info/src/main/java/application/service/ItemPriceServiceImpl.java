package application.service;

import adapters.inbound.rest.GetItemResource;
import application.port.ObjectManagementPort;
import application.port.URLValidationPort;
import application.port.PriceCheckPort;
import application.port.PriceLogRepository;
import domain.ItemPriceService;
import domain.model.BulkCheckResult;
import domain.model.PriceLog;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Anwendungs-Service (Use Case).
 */
@ApplicationScoped
public class ItemPriceServiceImpl implements ItemPriceService {

    @Inject
    ObjectManagementPort objectManagementPort;

    @Inject
    URLValidationPort urlValidationPort;

    @Inject
    PriceCheckPort priceCheckPort;

    @Inject
    PriceLogRepository priceLogRepository;
    @Inject
    GetItemResource getItemResource;


    @Override
    public List<BulkCheckResult> bulkCheckItems(List<Integer> itemIds) {
        List<BulkCheckResult> results = new ArrayList<>();

        // 1) Einmaliger Aufruf, um alle itemIds -> URL zu holen
        Map<Integer, String> reorderUrlMap = objectManagementPort.getReorderUrlsByIds(itemIds);

        for (Integer itemId : itemIds) {
            // Für jede ID erstellen wir ein "BulkCheckResult"-Objekt
            BulkCheckResult result = new BulkCheckResult();
            result.setItemId(itemId);

            try {
                String url = reorderUrlMap.get(itemId);
                if (url == null) {
                    throw new IllegalArgumentException("Keine URL gefunden für Item-ID: " + itemId);
                }
                if (!urlValidationPort.validateUrl(url)) {
                    throw new IllegalStateException("Ungültige URL: " + url);
                }

                // 2) Preis abfragen (kann Exception werfen)
                PriceLog priceLog = priceCheckPort.checkPrice(itemId, url);

                // 3) Nur wenn kein Fehler kam, wird gespeichert
                PriceLog stored = priceLogRepository.savePriceLog(priceLog);

                // 4) Status + Felder setzen
                result.setStatus("OK");
                result.setMessage("Preis und Verfügbarkeit erfolgreich gespeichert");
                result.setLogId(stored.getId());      // <-- DB-Generierte ID (oder null, wenn nicht generiert)
                result.setPrice(priceLog.getPrice());
                result.setAvailability(priceLog.getAvailability());

                // Falls availability == 0, Status anpassen
                if (priceLog.getAvailability() == 0) {
                    result.setStatus("UNAVAILABLE");
                    result.setMessage("Verfügbarkeit = 0");
                }

            } catch (IllegalArgumentException e) {
                // z.B. "Keine URL gefunden"
                result.setStatus("NOT_FOUND");
                result.setMessage(e.getMessage());

            } catch (IllegalStateException e) {
                // z.B. "Ungültige URL" oder "Unerwarteter HTTP-Status"
                result.setStatus("INVALID_URL");
                result.setMessage(e.getMessage());

            } catch (Exception e) {
                // Andere unvorhergesehene Fehler
                result.setStatus("ERROR");
                result.setMessage(e.getMessage());
            }

            // 5) Das Ergebnis in Gesamtliste packen – egal ob Fehler oder Erfolg
            results.add(result);
        }

        return results;
    }




    //REST Funktionen (Speichern keine Log-Einträge!)

    public PriceLog checkPriceByUrl(String url) {
        if (url == null || url.isEmpty()) {
            throw new IllegalArgumentException("URL darf nicht leer sein.");
        }
        // Validierung der URL
        if (!urlValidationPort.validateUrl(url)) {
            throw new IllegalStateException("URL ist ungültig oder nicht erreichbar: " + url);
        }
        Integer itemId = 0;
        return priceCheckPort.checkPrice(itemId, url);
    }


    public PriceLog checkPriceById(Integer itemId) {
        if (itemId == null) {
            throw new IllegalArgumentException("Item ID darf nicht null sein.");
        }
        // Hole die URL für die Item-ID
        Map<Integer, String> reorderUrlMap = objectManagementPort.getReorderUrlsByIds(List.of(itemId));
        String url = reorderUrlMap.get(itemId);

        if (url == null) {
            throw new IllegalArgumentException("Keine URL gefunden für Item ID: " + itemId);
        }
        // Validierung der URL
        if (!urlValidationPort.validateUrl(url)) {
            throw new IllegalStateException("URL ist ungültig oder nicht erreichbar: " + url);
        }
        return priceCheckPort.checkPrice(itemId, url);
    }


    public PriceLog getLogById(Integer logId) {
        if (logId == null) {
            throw new IllegalArgumentException("Log ID darf nicht null sein.");
        }

        // Log aus der Datenbank abrufen
        Optional<PriceLog> logOpt = priceLogRepository.findById(logId);

        return logOpt.orElseThrow(() ->
                new IllegalArgumentException("Kein Log mit ID " + logId + " gefunden.")
        );
    }


}
