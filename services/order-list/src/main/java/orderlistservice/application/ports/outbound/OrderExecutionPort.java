package orderlistservice.application.ports.outbound;

import jakarta.ws.rs.core.Response;

public interface OrderExecutionPort {
    Response executeOrder(String orderUrl, Integer quantity);
}
