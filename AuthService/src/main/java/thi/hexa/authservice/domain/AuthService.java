package thi.hexa.authservice.domain;


import thi.hexa.authservice.domain.exception.AuthException;

public interface AuthService {

    String login(int user_id, String password) throws AuthException;
}
