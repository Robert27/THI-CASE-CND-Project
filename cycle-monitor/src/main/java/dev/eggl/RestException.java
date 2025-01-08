package dev.eggl;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

public class RestException extends WebApplicationException {

    public RestException(String message, Response.Status status) {
        super(Response.status(status).entity(message).type(MediaType.TEXT_PLAIN).build());
    }


}
