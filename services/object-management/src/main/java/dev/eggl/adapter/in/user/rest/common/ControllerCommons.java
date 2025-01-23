package dev.eggl.adapter.in.user.rest.common;

import io.quarkus.security.AuthenticationFailedException;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.ServerErrorException;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;

public final class ControllerCommons {

    private ControllerCommons() {
    }

    public static ClientErrorException clientErrorException(Response.Status status, String message) {
        return new ClientErrorException(errorResponse(status, message));
    }

    public static ServerErrorException serverErrorException(Response.Status status, String message) {
        return new ServerErrorException(errorResponse(status, message));
    }

    public static Response errorResponse(Response.Status status, String message) {
        ErrorEntity errorEntity = new ErrorEntity(status.getStatusCode(), message);
        return Response.status(status).entity(errorEntity).build();
    }

    public static String extractJwt(HttpHeaders headers) {
        String prefix = "Bearer ";
        String authHeader = headers.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith(prefix)) {
            throw new AuthenticationFailedException("Missing or invalid authorization header");
        }
        return authHeader.substring(prefix.length());
    }
}
