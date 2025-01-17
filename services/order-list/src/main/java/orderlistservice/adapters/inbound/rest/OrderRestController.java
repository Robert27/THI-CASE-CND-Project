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
import java.util.List;

@Path("/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class OrderRestController {

    @Inject
    OrderService orderService;

    @Inject
    JsonWebToken jwt;

    @Inject
    @Claim(standard = Claims.sub)
    String userId;

//    @POST
//    @Path("/generate")
//    @RolesAllowed({"user"})
//    public Response generateOrderList(GenerateOrderRequest request) {
//        orderUseCase.generateOrderList(request.getUserId(), request.getItemIds(), request.getCycleDate());
//        return Response.ok().build();
//    }

    @GET
    @Authenticated
    public Response getOpenOrders() {
        List<OrderObject> domainOrders = orderService.getOpenOrders(Integer.parseInt(userId));
        List<GetOpenOrdersResponse> dtoList = OrderMapper.toDtoList(domainOrders);
        return Response.ok(dtoList).build();
    }

    @POST
    @Path("/perform")
    @Authenticated
    public Response performOrder(PerformOrderRequest request) {
        int uid = Integer.parseInt(userId);

        // Rufe Domain-Service auf
        PerformOrderResult domainResult = orderService.performOrder(uid, request.getItemId(), request.getQuantity());

        PerformOrderResponse responseDto = new PerformOrderResponse(
                domainResult.getStatusCode(),
                domainResult.getMessage()
        );

        return Response.status(domainResult.getStatusCode())
                .entity(responseDto)
                .build();
    }

    @POST
    @Path("/abort/{itemId}")
    @Authenticated
    public Response abortOrder(@PathParam("itemId") Integer itemId) {
        int uid = Integer.parseInt(userId);

        boolean isAborted = orderService.abortOrder(uid, itemId);
        Integer newStatus = isAborted ? 200 : 500;
        String message = isAborted ? "Order successfully aborted." : "Error aborting order.";
        AbortOrderResponse responseDto = new AbortOrderResponse(newStatus, message);
        return Response.ok(responseDto).build();
    }
}