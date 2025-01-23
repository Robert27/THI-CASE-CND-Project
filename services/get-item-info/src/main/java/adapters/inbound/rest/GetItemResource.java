package adapters.inbound.rest;

import domain.ItemPriceService;
import domain.model.PriceLog;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.enterprise.context.RequestScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/")
@RequestScoped
public class GetItemResource {

    private static final Logger LOG = LoggerFactory.getLogger(GetItemResource.class);

    @Inject
    ItemPriceService itemPriceService;

    @POST
    @Path("/check-price/url")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkPriceByUrl(String url) {
        LOG.info("checkPriceByUrl aufgerufen mit url={}", url);

        try {
            PriceLog priceLog = itemPriceService.checkPriceByUrl(url);
            LOG.debug("Rückgabe PriceLog: {}", priceLog);

            return Response.ok(priceLog).build();
        } catch (IllegalArgumentException e) {
            // Hier war z.B. die URL null oder leer
            LOG.warn("IllegalArgumentException in checkPriceByUrl: {}", e.getMessage());
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        } catch (IllegalStateException e) {
            // URL war ungültig oder nicht erreichbar
            LOG.warn("IllegalStateException in checkPriceByUrl: {}", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        } catch (Exception e) {
            // Andere (unbekannte) Fehler
            LOG.error("Unbekannter Fehler in checkPriceByUrl", e);
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
        LOG.info("checkPriceById aufgerufen mit itemId={}", itemId);

        try {
            PriceLog priceLog = itemPriceService.checkPriceById(itemId);
            LOG.debug("Rückgabe PriceLog: {}", priceLog);

            return Response.ok(priceLog).build();
        } catch (IllegalArgumentException e) {
            LOG.warn("IllegalArgumentException in checkPriceById: {}", e.getMessage());
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        } catch (IllegalStateException e) {
            LOG.warn("IllegalStateException in checkPriceById: {}", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        } catch (Exception e) {
            LOG.error("Unbekannter Fehler in checkPriceById", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/get-log/{logId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLogById(@PathParam("logId") Integer logId) {
        LOG.info("getLogById aufgerufen mit logId={}", logId);

        try {
            PriceLog log = itemPriceService.getLogById(logId);
            LOG.debug("Rückgabe PriceLog: {}", log);

            return Response.ok(log).build();
        } catch (IllegalArgumentException e) {
            LOG.warn("IllegalArgumentException in getLogById: {}", e.getMessage());
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        } catch (Exception e) {
            LOG.error("Unbekannter Fehler in getLogById", e);
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

