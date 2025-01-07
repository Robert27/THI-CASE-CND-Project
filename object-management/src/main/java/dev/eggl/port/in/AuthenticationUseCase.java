package dev.eggl.port.in;

import dev.eggl.domain.model.AuthenticatedUser;

public interface AuthenticationUseCase {
    AuthenticatedUser authenticate(String token);
}
