package orderlistservice.adapters.inbound.rest;

import io.quarkus.security.Authenticated;
import orderlistservice.domain.OrderService;

import orderlistservice.domain.model.OrderObject;
import orderlistservice.domain.model.PerformOrderResult;
import orderlistservice.adapters.inbound.rest.dto.GetOpenOrdersResponse;
import orderlistservice.adapters.inbound.rest.dto.PerformOrderRequest;


import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;
import java.util.List;

@Path("/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class OrderController {

    @Inject
    OrderService orderUseCase;

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
        List<OrderObject> domainOrders = orderUseCase.getOpenOrders(Integer.parseInt(userId));
        List<GetOpenOrdersResponse> dtoList = OrderMapper.toDtoList(domainOrders);
        return Response.ok(dtoList).build();
    }

    @POST
    @Path("/perform")
    @Authenticated
    public Response performOrder(PerformOrderRequest request) {
        PerformOrderResult result = orderUseCase.performOrder(
                Integer.parseInt(userId),
                request.getItemId(),
                request.getQuantity(),
                request.getAuthToken()
        );
        // Response
        return Response.status(result.getStatusCode())
                .entity(result.getMessage())
                .build();
    }


    @POST
    @Path("/abort/{itemId}")
    @Authenticated
    public Response abortOrder(@PathParam("itemId") Integer itemId) {
        orderUseCase.abortOrder(Integer.parseInt(userId), itemId);
        return Response.ok().build();
    }
}