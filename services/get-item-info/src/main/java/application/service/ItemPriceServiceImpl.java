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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Anwendungs-Service (Use Case).
 */
@ApplicationScoped
public class ItemPriceServiceImpl implements ItemPriceService {

    private static final Logger LOG = LoggerFactory.getLogger(ItemPriceServiceImpl.class);

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
        LOG.info("bulkCheckItems aufgerufen mit itemIds={}", itemIds);

        List<BulkCheckResult> results = new ArrayList<>();

        // 1) Einmaliger Aufruf, um alle itemIds -> URL zu holen
        Map<Integer, String> reorderUrlMap = objectManagementPort.getReorderUrlsByIds(itemIds);
        LOG.debug("Reorder-URL-Map: {}", reorderUrlMap);

        for (Integer itemId : itemIds) {
            BulkCheckResult result = new BulkCheckResult();
            result.setItemId(itemId);

            try {
                String url = reorderUrlMap.get(itemId);
                LOG.debug("Verarbeite itemId={}, url={}", itemId, url);

                if (url == null) {
                    throw new IllegalArgumentException("Keine URL gefunden für Item-ID: " + itemId);
                }
                if (!urlValidationPort.validateUrl(url)) {
                    throw new IllegalStateException("Ungültige URL: " + url);
                }

                // 2) Preis abfragen
                PriceLog priceLog = priceCheckPort.checkPrice(itemId, url);
                LOG.debug("Preis-Check erfolgreich: itemId={}, priceLog={}", itemId, priceLog);

                // 3) Speichern
                PriceLog stored = priceLogRepository.savePriceLog(priceLog);
                LOG.info("PriceLog gespeichert mit ID={}", stored.getId());

                // 4) Status + Felder setzen
                result.setStatus("OK");
                result.setMessage("Preis und Verfügbarkeit erfolgreich gespeichert");
                result.setLogId(stored.getId());
                result.setPrice(priceLog.getPrice());
                result.setAvailability(priceLog.getAvailability());

                // Falls availability == 0, Status anpassen
                if (priceLog.getAvailability() == 0) {
                    result.setStatus("UNAVAILABLE");
                    result.setMessage("Verfügbarkeit = 0");
                    LOG.warn("Verfügbarkeit 0 für itemId={}", itemId);
                }

            } catch (IllegalArgumentException e) {
                LOG.warn("Fehler bei itemId={}: {}", itemId, e.getMessage());
                result.setStatus("NOT_FOUND");
                result.setMessage(e.getMessage());

            } catch (IllegalStateException e) {
                LOG.warn("Fehler bei itemId={}: {}", itemId, e.getMessage());
                result.setStatus("INVALID_URL");
                result.setMessage(e.getMessage());

            } catch (Exception e) {
                LOG.error("Unbekannter Fehler bei itemId={}", itemId, e);
                result.setStatus("ERROR");
                result.setMessage(e.getMessage());
            }

            results.add(result);
        }

        LOG.info("bulkCheckItems fertig. Resultate={}", results);
        return results;
    }


    // REST-Funktionen (speichern keine Log-Einträge!)

    public PriceLog checkPriceByUrl(String url) {
        LOG.debug("checkPriceByUrl aufgerufen mit url={}", url);

        if (url == null || url.isEmpty()) {
            throw new IllegalArgumentException("URL darf nicht leer sein.");
        }

        // Validierung der URL
        if (!urlValidationPort.validateUrl(url)) {
            throw new IllegalStateException("URL ist ungültig oder nicht erreichbar: " + url);
        }

        // Item-ID hier 0 als Platzhalter
        Integer itemId = 0;
        PriceLog result = priceCheckPort.checkPrice(itemId, url);
        LOG.debug("Ergebnis checkPriceByUrl: {}", result);
        return result;
    }

    public PriceLog checkPriceById(Integer itemId) {
        LOG.debug("checkPriceById aufgerufen mit itemId={}", itemId);

        if (itemId == null) {
            throw new IllegalArgumentException("Item ID darf nicht null sein.");
        }

        // Hole die URL für die Item-ID
        Map<Integer, String> reorderUrlMap = objectManagementPort.getReorderUrlsByIds(List.of(itemId));
        String url = reorderUrlMap.get(itemId);

        if (url == null) {
            throw new IllegalArgumentException("Keine URL gefunden für Item ID: " + itemId);
        }
        if (!urlValidationPort.validateUrl(url)) {
            throw new IllegalStateException("URL ist ungültig oder nicht erreichbar: " + url);
        }

        PriceLog result = priceCheckPort.checkPrice(itemId, url);
        LOG.debug("Ergebnis checkPriceById: {}", result);
        return result;
    }

    public PriceLog getLogById(Integer logId) {
        LOG.debug("getLogById aufgerufen mit logId={}", logId);

        if (logId == null) {
            throw new IllegalArgumentException("Log ID darf nicht null sein.");
        }

        // Log aus der Datenbank abrufen
        Optional<PriceLog> logOpt = priceLogRepository.findById(logId);

        PriceLog priceLog = logOpt.orElseThrow(() ->
                new IllegalArgumentException("Kein Log mit ID " + logId + " gefunden.")
        );

        LOG.debug("Gefundenes PriceLog: {}", priceLog);
        return priceLog;
    }
}

