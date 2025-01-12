package orderlistservice.adapters.outbound.rest.orderexecution;

import orderlistservice.adapters.outbound.rest.orderexecution.dto.OrderExecutionRequest;
import orderlistservice.application.ports.outbound.OrderExecutionPort;
import orderlistservice.adapters.outbound.rest.orderexecution.dto.*;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.rest.client.RestClientBuilder;

import jakarta.ws.rs.core.Response;
import java.net.URL;

@ApplicationScoped
public class OrderExecutionClientImpl implements OrderExecutionPort {

    @Override
    public Response executeOrder(String orderUrl, Integer quantity) {
        OrderExecutionRequest request = new OrderExecutionRequest();
        try {
            OrderExecutionService service = RestClientBuilder.newBuilder()
                    .baseUrl(new URL(orderUrl))
                    .build(OrderExecutionService.class);

            return service.postOrder(request);

        } catch (Exception e) {
            throw new RuntimeException("Failed to execute order at " + orderUrl, e);
        }
    }
}