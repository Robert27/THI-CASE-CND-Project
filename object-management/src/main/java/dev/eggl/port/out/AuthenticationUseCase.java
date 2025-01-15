package dev.eggl.port.out;

import dev.eggl.domain.model.AuthenticatedUser;

public interface AuthenticationUseCase {
    AuthenticatedUser authenticate(String token);
}
