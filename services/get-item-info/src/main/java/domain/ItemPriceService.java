package domain;

import domain.model.BulkCheckResult;
import domain.model.PriceLog;

import java.util.List;
import java.util.Map;

/**
 * Definiert die Hauptusecases rund um
 * Preisabfrage und Logging.
 */
public interface ItemPriceService {

    List<BulkCheckResult> bulkCheckItems(List<Integer> itemIds);

    PriceLog checkPriceByUrl(String url);

    PriceLog checkPriceById(Integer itemId);

    PriceLog getLogById(Integer logId);

}