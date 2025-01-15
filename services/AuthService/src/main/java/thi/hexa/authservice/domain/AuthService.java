package thi.hexa.authservice.domain;


import thi.hexa.authservice.domain.exception.AuthException;

public interface AuthService {

    String login(String username, String password) throws AuthException;
}
