package dev.eggl.adapter.jwt;

import dev.eggl.adapter.out.jwt.JwtAuthenticationController;
import dev.eggl.domain.model.AuthenticatedUser;
import io.quarkus.security.AuthenticationFailedException;
import io.smallrye.jwt.auth.principal.DefaultJWTCallerPrincipal;
import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.auth.principal.ParseException;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jose4j.jwt.JwtClaims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class JwtAuthenticationTest {

    @Mock
    JWTParser jwtParser;

    @InjectMocks
    JwtAuthenticationController jwtAuthenticationController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAuthenticate_ValidToken() throws ParseException {
        String token = "valid.jwt.token";
        JwtClaims claims = new JwtClaims();
        claims.setSubject("1");
        claims.setStringClaim("username", "testuser");

        JsonWebToken jwt = new DefaultJWTCallerPrincipal(claims);

        when(jwtParser.parse(token)).thenReturn(jwt);

        AuthenticatedUser user = jwtAuthenticationController.authenticate(token);

        assertNotNull(user);
        assertEquals(1, user.getUserId());
        assertEquals("testuser", user.getUsername());
    }

    @Test
    public void testAuthenticate_MissingSubject() throws ParseException {
        String token = "valid.jwt.token";
        JwtClaims claims = new JwtClaims();
        claims.setStringClaim("username", "testuser");

        JsonWebToken jwt = new DefaultJWTCallerPrincipal(claims);

        when(jwtParser.parse(token)).thenReturn(jwt);

        // should return io.quarkus.security.AuthenticationFailedException: Missing
        // subject or username
        assertThrows(AuthenticationFailedException.class, () -> {
            jwtAuthenticationController.authenticate(token);
        });

    }

    @Test
    public void testAuthenticate_InvalidToken() throws ParseException {
        String token = "invalid.jwt.token";

        when(jwtParser.parse(token)).thenThrow(new ParseException("Invalid token"));

        assertThrows(AuthenticationFailedException.class, () -> {
            jwtAuthenticationController.authenticate(token);
        });
    }

}
