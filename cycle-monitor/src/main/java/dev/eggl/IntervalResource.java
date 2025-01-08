package dev.eggl;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;


import java.time.Instant;
import java.util.List;

@Path("/interval")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class IntervalResource {

    @Inject
    IntervalRepository intervalRepository;

    @Inject
    JsonWebToken jwt;

    @Claim(standard = Claims.sub)
    String userId;

    @GET
    public List<IntervalEntity> getAllIntervals() {

        System.out.println("User: " + userId);
        return intervalRepository.find("userId", Integer.parseInt(userId)).list();
    }

    @GET
    @Path("/{id}")
    public IntervalEntity getById(@PathParam("id") Integer id) {
        IntervalEntity interval = intervalRepository.find("id = ?1 AND userId = ?2", id, Integer.parseInt(userId)).firstResult();
        if (interval == null) {
            throw new WebApplicationException("Interval not found", 404);
        }
        return interval;
    }

@POST
@Transactional
public Response createInterval(IntervalDTO intervalDTO) {
if (intervalDTO.name == null || intervalDTO.interval == null) {
    throw new RestException("Name and interval cannot be null", Response.Status.BAD_REQUEST);
    }

if (intervalDTO.interval < 60) {
    throw new RestException("Interval must be at least 60 seconds", Response.Status.BAD_REQUEST);
}
if (intervalRepository.find("name = ?1 AND userId = ?2", intervalDTO.name, Integer.parseInt(userId)).firstResult() != null) {
    throw new RestException("Interval with the same name already exists", Response.Status.BAD_REQUEST);
}
    Instant startTime = intervalDTO.enabled != null && intervalDTO.enabled ? Instant.now() : null;
    intervalRepository.persist(new IntervalEntity(intervalDTO.name, Integer.parseInt(userId), intervalDTO.interval, startTime));
    return Response.status(Response.Status.CREATED).entity(intervalDTO).build();
}

    @PATCH
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response patchInterval(@PathParam("id") Integer id, IntervalDTO updatedInterval) {
        IntervalEntity existingInterval = intervalRepository.find("id = ?1 AND userId = ?2", id, Integer.parseInt(userId)).firstResult();
        if (existingInterval == null) {
            throw new RestException("Interval not found", Response.Status.NOT_FOUND);
        }

        if (updatedInterval.name != null) {
            existingInterval.setName(updatedInterval.name);
        }

        if (updatedInterval.interval != null) {
            if (updatedInterval.interval < 60) {
                throw new RestException("Interval must be at least 60 seconds", Response.Status.BAD_REQUEST);
            }
            existingInterval.setInterval(updatedInterval.interval);
        }

        if (updatedInterval.enabled != null) {
            existingInterval.setStartTime(updatedInterval.enabled ? Instant.now() : null);
        }

        intervalRepository.persist(existingInterval);
        return Response.ok(existingInterval).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteInterval(@PathParam("id") Integer id) {
        IntervalEntity existingInterval = intervalRepository.find("id = ?1 AND userId = ?2", id, Integer.parseInt(userId)).firstResult();
        if (existingInterval == null) {
            throw new RestException("Interval not found", Response.Status.NOT_FOUND);
        }
        intervalRepository.delete(existingInterval);
        return Response.noContent().build();
    }


    @GET
    @Path("/active")
    public List<IntervalEntity> getActiveIntervals() {
        Instant now = Instant.now();
        return intervalRepository.find(
                "startTime IS NOT NULL AND startTime + make_interval(secs => interval) " +
                        "BETWEEN ?1 AND ?2",
                now.minusSeconds(30),
                now.plusSeconds(30)
        ).list();
    }

    @GET
    @Path("/name/{name}")
    public IntervalEntity findByName(@PathParam("name") String name) {
        return intervalRepository.findByName(name);
    }
}
