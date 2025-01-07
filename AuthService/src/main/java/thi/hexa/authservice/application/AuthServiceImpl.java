package thi.hexa.authservice.application;

import org.springframework.stereotype.Service;
import thi.hexa.authservice.adapter.restclient.UserClient;
import thi.hexa.authservice.domain.AuthService;
import thi.hexa.authservice.domain.User;
import thi.hexa.authservice.domain.exception.AuthException;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserClient userClient;

    public AuthServiceImpl(UserClient userClient) {
        this.userClient = userClient;
    }

    @Override
    public String login(int user_id, String password) throws AuthException {
        System.out.println(user_id + " " + password);
        if (!userClient.validatePassword(user_id, password)) {
            throw new AuthException("failded to authenticate");
        }
        return JwtUtil.generateToken(userClient.getUser(user_id));
    }
}
