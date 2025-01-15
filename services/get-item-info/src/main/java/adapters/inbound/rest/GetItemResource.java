package adapters.inbound.rest;

import adapters.inbound.rest.dto.BulkCheckRequest;
import adapters.inbound.rest.dto.BulkCheckResultDTO;
import adapters.inbound.rest.dto.BulkCheckResponse;
import domain.ItemPriceService;
import domain.model.PriceLog;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.enterprise.context.RequestScoped;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("/")
@RequestScoped
public class GetItemResource {

    @Inject
    ItemPriceService itemPriceService;


    @POST
    @Path("/check-price/url")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkPriceByUrl(String url) {
        try {
            // Aufruf der Service-Methode
            PriceLog priceLog = itemPriceService.checkPriceByUrl(url);
            return Response.ok(priceLog).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        } catch (IllegalStateException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(e.getMessage())
                    .build();
        }
    }


    @POST
    @Path("/check-price/id")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkPriceById(Integer itemId) {
        try {
            // Aufruf der Service-Methode
            PriceLog priceLog = itemPriceService.checkPriceById(itemId);
            return Response.ok(priceLog).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        } catch (IllegalStateException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/get-log/{logId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLogById(@PathParam("logId") Integer logId) {
        try {
            // Aufruf der Service-Methode
            PriceLog log = itemPriceService.getLogById(logId);
            return Response.ok(log).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(e.getMessage())
                    .build();
        }
    }

//    @POST
//    @Path("/bulk-check")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response bulkCheck(BulkCheckRequest request) {
//        // 1) Domain-UseCase
//        List<BulkCheckResult> domainResults = itemPriceService.bulkCheckItems(request.getItemIds());
//
//        // 2) Mapping Domain -> DTO
//        List<BulkCheckResultDTO> dtoResults = domainResults.stream()
//                .map(res -> new BulkCheckResultDTO(
//                        res.getItemId(),
//                        res.getStatus(),
//                        res.getMessage(),
//                        res.getLogId(),
//                        res.getPrice(),
//                        res.getAvailability()
//                ))
//                .collect(Collectors.toList());
//
//        // 3) Evtl. in BulkCheckResponse einbetten
//        BulkCheckResponse response = new BulkCheckResponse(dtoResults);
//        return Response.ok(response).build();
//    }
//

//    @POST
//    @Path("/url")
//    @Consumes(MediaType.TEXT_PLAIN)
//    @Produces(MediaType.TEXT_PLAIN)
//    public Response getItemUrl(String itemId) {
//        try {
//            String url = itemPriceService.getUrlForItemId(itemId);
//            return Response.ok(url).build();
//        } catch (IllegalArgumentException e) {
//            return Response.status(Response.Status.NOT_FOUND)
//                    .entity(e.getMessage())
//                    .build();
//        } catch (IllegalStateException e) {
//            // URL war ungültig
//            return Response.status(Response.Status.BAD_REQUEST)
//                    .entity(e.getMessage())
//                    .build();
//        }
//    }

//    @POST
//    @Path("/check-price")
//    @Consumes(MediaType.TEXT_PLAIN)
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response checkPrice(String itemId) {
//        try {
//            // Use Case aufrufen
//            String url = itemPriceService.getUrlForItemId(itemId);
//            PriceLog priceLog = itemPriceService.checkAndStorePrice(itemId, url);
//            // Als JSON zurückgeben
//            return Response.ok(priceLog).build();
//
//        } catch (IllegalArgumentException e) {
//            return Response.status(Response.Status.NOT_FOUND)
//                    .entity(e.getMessage())
//                    .build();
//        } catch (IllegalStateException e) {
//            return Response.status(Response.Status.BAD_REQUEST)
//                    .entity(e.getMessage())
//                    .build();
//        }
//    }
}

