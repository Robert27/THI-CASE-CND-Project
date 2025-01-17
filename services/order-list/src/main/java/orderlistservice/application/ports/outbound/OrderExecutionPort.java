package orderlistservice.application.ports.outbound;

import jakarta.ws.rs.core.Response;
import orderlistservice.adapters.outbound.rest.orderexecution.dto.OrderExecutionResponse;

public interface OrderExecutionPort {
    boolean  executeOrder(String orderUrl, Integer quantity);
}
