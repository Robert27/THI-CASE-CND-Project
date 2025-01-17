package dev.eggl.config;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.time.LocalDate;


// This is just for testing and demonstration purposes. The mock configuration is not intended to be used in production.
@Path("/mock")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MockController {
    @Inject
    MockConfiguration mockConfig;

    @GET
    @Path("/enabled")
    public boolean isMockEnabled() {
        return mockConfig.isMockEnabled();
    }

    @GET
    public String getCurrentMockDate() {
        return mockConfig.getMockDate().toString();
    }

    @POST
    @Path("/date")
    @Consumes(MediaType.TEXT_PLAIN)
    public void setMockDate(String date) {
        mockConfig.setMockDate(LocalDate.parse(date.trim()));
    }
}
