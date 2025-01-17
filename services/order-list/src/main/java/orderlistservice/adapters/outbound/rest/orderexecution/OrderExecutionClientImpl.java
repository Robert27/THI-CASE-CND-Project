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
    public boolean executeOrder(String orderUrl, Integer quantity) {
        try {
            OrderExecutionService service = RestClientBuilder.newBuilder()
                    .baseUrl(new URL(orderUrl))
                    .build(OrderExecutionService.class);

            OrderExecutionRequest request = new OrderExecutionRequest(quantity);
            OrderExecutionResponse externalResponse = service.postOrder(request);
            return externalResponse.isSuccess();

        } catch (Exception e) {
            return false;
        }
    }
}