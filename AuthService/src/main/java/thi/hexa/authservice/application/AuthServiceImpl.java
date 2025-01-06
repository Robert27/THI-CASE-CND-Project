package thi.hexa.authservice.application;

import org.springframework.stereotype.Service;
import thi.hexa.authservice.adapter.restclient.UserClient;
import thi.hexa.authservice.domain.AuthService;
import thi.hexa.authservice.domain.User;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserClient userClient;

    public AuthServiceImpl(UserClient userClient) {
        this.userClient = userClient;
    }

    @Override
    public String login(String username, String password) {
        if (!userClient.validatePassword(username,password)){
            System.out.println(username + " " + password);
            return JwtUtil.generateToken(username);
        }

        return null;
    }

    @Override
    public String decode(String token) {
        return null;
    }

    @Override
    public String encode(String token) {
        return "";
    }


    @Override
    public String generateToken(User user) {

        return null;
    }
}
