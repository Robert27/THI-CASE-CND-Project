package orderlistservice.adapters.inbound.rest;

import io.quarkus.security.Authenticated;
import orderlistservice.domain.OrderService;
import orderlistservice.domain.model.OrderObject;
import orderlistservice.domain.model.PerformOrderResult;
import orderlistservice.adapters.inbound.rest.dto.GetOpenOrdersResponse;
import orderlistservice.adapters.inbound.rest.dto.PerformOrderRequest;
import orderlistservice.adapters.inbound.rest.dto.*;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logging.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class OrderRestController {

    private static final Logger LOGGER = Logger.getLogger(OrderRestController.class);

    @Inject
    OrderService orderService;

    @Inject
    JsonWebToken jwt;

    @Inject
    @Claim(standard = Claims.sub)
    String userId;


    @GET
    @Authenticated
    public Response getOpenOrders() {
        try {
            List<OrderObject> domainOrders = orderService.getOpenOrders(Integer.parseInt(userId));
            List<GetOpenOrdersResponse> dtoList = OrderMapper.toDtoList(domainOrders);
            LOGGER.infof("Retrieved %d open orders for user %s.", dtoList.size(), userId);
            return Response.ok(dtoList).build();
        } catch (Exception e) {
            LOGGER.errorf("Error retrieving open orders for user %s: %s", userId, e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to retrieve open orders.");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(error)
                    .build();
        }
    }

    @POST
    @Path("/perform")
    @Authenticated
    public Response performOrder(PerformOrderRequest request) {
        int uid = Integer.parseInt(userId);
        try {
            // Rufe Domain-Service auf
            PerformOrderResult domainResult = orderService.performOrder(uid, request.getItemId(), request.getQuantity());

            PerformOrderResponse responseDto = new PerformOrderResponse(
                    domainResult.getStatusCode(),
                    domainResult.getMessage()
            );

            LOGGER.infof("Order performed with status %d for user %s.", domainResult.getStatusCode(), userId);
            return Response.status(domainResult.getStatusCode())
                    .entity(responseDto)
                    .build();
        } catch (Exception e) {
            LOGGER.errorf("Error performing order for user %s: %s", userId, e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new PerformOrderResponse(500, "Failed to perform order."))
                    .build();
        }
    }

    @POST
    @Path("/abort/{itemId}")
    @Authenticated
    public Response abortOrder(@PathParam("itemId") Integer itemId) {
        int uid = Integer.parseInt(userId);
        try {
            boolean isAborted = orderService.abortOrder(uid, itemId);
            Integer newStatus = isAborted ? 200 : 500;
            String message = isAborted ? "Order successfully aborted." : "Error aborting order.";
            AbortOrderResponse responseDto = new AbortOrderResponse(newStatus, message);

            if (isAborted) {
                LOGGER.infof("Order for Item ID %d aborted successfully for user %s.", itemId, userId);
                return Response.ok(responseDto).build();
            } else {
                LOGGER.warnf("Failed to abort order for Item ID %d for user %s.", itemId, userId);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(responseDto).build();
            }
        } catch (Exception e) {
            LOGGER.errorf("Error aborting order for Item ID %d for user %s: %s", itemId, userId, e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new AbortOrderResponse(500, "Failed to abort order."))
                    .build();
        }
    }
}