package application.port;

import domain.model.PriceLog;

public interface PriceCheckPort {

    PriceLog checkPrice(Integer itemId, String url);
}
