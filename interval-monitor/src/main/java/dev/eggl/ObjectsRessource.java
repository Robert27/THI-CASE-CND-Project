package dev.eggl;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import io.smallrye.mutiny.Uni;

@Path("/objects")
public class ObjectsRessource {

    @Inject
    ObjectClientService objectClientService;

    @GET
    @Path("/{userId}")
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<String> getObjects(@PathParam("userId") Integer userId) {
        return objectClientService.getObjects(userId);
    }
}
