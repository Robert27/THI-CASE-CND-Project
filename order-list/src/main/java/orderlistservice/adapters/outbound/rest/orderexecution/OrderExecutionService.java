package orderlistservice.adapters.outbound.rest.orderexecution;

import orderlistservice.adapters.outbound.rest.orderexecution.dto.*;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.core.MediaType;
import orderlistservice.adapters.outbound.rest.orderexecution.dto.OrderExecutionRequest;

@Path("/")
public interface OrderExecutionService {

    @POST
    @Path("/order")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response postOrder(OrderExecutionRequest request);
}