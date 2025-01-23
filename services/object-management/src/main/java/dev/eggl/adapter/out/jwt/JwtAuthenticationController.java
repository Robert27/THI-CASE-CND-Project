package dev.eggl.adapter.out.jwt;

import dev.eggl.domain.model.AuthenticatedUser;
import dev.eggl.port.out.AuthenticationUseCase;
import io.quarkus.security.AuthenticationFailedException;
import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.auth.principal.ParseException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class JwtAuthenticationController implements AuthenticationUseCase {

    @Inject
    JWTParser jwtParser;

    @Override
    public AuthenticatedUser authenticate(String token) {
        try {
            var jwt = jwtParser.parse(token);

            // Extract claims
            String userId = jwt.getClaim("sub");
            String username = jwt.getClaim("username");

            if (userId == null || username == null) {
                System.out.println("Invalid JWT provided");
                throw new AuthenticationFailedException("Missing subject or username");
            }

            // Smallrye JWT will automatically validate the token based on the public key and the expiration time
            return new AuthenticatedUser(Integer.parseInt(userId), username);

        } catch (ParseException e) {
            System.out.println("Failed to verify JWT token");
            throw new AuthenticationFailedException("Invalid token", e);
        }
    }
}
