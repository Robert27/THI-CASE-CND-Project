package application.port;

import domain.model.PriceLog;
import java.util.List;
import java.util.Optional;

public interface PriceLogRepository {
    PriceLog savePriceLog(PriceLog log);
    Optional<PriceLog> findById(Integer logId);


//    List<PriceLog> findAllByItemId(String itemId);
//    // optional: findLastPriceLog(String itemId) ...
//    //kann Methoden zur Abfrage nach itemId beinhalten
}
