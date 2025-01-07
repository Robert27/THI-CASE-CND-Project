package thi.hexa.authservice.adapter.api.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import thi.hexa.authservice.adapter.api.rest.dto.LoginReply;
import thi.hexa.authservice.adapter.api.rest.dto.LoginRequest;
import thi.hexa.authservice.domain.AuthService;
import thi.hexa.authservice.domain.exception.AuthException;

@RestController
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/login")
    LoginReply login(@RequestBody LoginRequest loginRequest) {

        String token = null;
        try {
            token = authService.login(loginRequest.getUser_id(), loginRequest.getPassword());
        } catch (AuthException e) {
            return new LoginReply(null,false,e.getMessage());
        }
        return new LoginReply(token,true,"");
    }
}
