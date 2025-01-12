package orderlistservice.application.ports.outbound;

import orderlistservice.domain.model.PriceResult;

import java.util.List;

/**
 * Outbound Port für den Price Monitoring Service.
 */
public interface PriceMonitoringPort {
    List<PriceResult> checkPrices(List<Integer> itemIds);
}
