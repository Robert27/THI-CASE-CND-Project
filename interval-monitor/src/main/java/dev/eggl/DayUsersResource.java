package dev.eggl;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import io.smallrye.mutiny.Uni;

import java.util.List;

@Path("/day-users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DayUsersResource {

    @Inject
    ObjectClientService objectClientService;

    @GET
    public Uni<ObjectClientService.DayUsersResponse> getDayUsers(@QueryParam("weekday") int weekday, @QueryParam("userIds") List<Integer> userIds) {
        System.out.println("Received request to get day users for weekday " + weekday + " and user IDs " + userIds);
        return objectClientService.findAllDayUsers(weekday, userIds);
    }
}
