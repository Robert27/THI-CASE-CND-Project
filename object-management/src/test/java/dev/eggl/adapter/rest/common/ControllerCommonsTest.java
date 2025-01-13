package dev.eggl.adapter.rest.common;

import io.quarkus.security.AuthenticationFailedException;
import jakarta.ws.rs.core.HttpHeaders;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ControllerCommonsTest {

    @Test
    public void testExtractJwt_ValidHeader() {
        HttpHeaders headers = mock(HttpHeaders.class);
        when(headers.getHeaderString(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer valid.jwt.token");

        String token = ControllerCommons.extractJwt(headers);

        assertEquals("valid.jwt.token", token);
    }

    @Test
    public void testExtractJwt_MissingHeader() {
        HttpHeaders headers = mock(HttpHeaders.class);
        when(headers.getHeaderString(HttpHeaders.AUTHORIZATION)).thenReturn(null);

        assertThrows(AuthenticationFailedException.class, () -> {
            ControllerCommons.extractJwt(headers);
        });
    }

    @Test
    public void testExtractJwt_InvalidHeader() {
        HttpHeaders headers = mock(HttpHeaders.class);
        when(headers.getHeaderString(HttpHeaders.AUTHORIZATION)).thenReturn("InvalidHeader");

        assertThrows(AuthenticationFailedException.class, () -> {
            ControllerCommons.extractJwt(headers);
        });
    }
}
