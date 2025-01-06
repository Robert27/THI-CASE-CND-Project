package thi.hexa.authservice.adapter.api.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import thi.hexa.authservice.adapter.api.rest.dto.LoginRequest;
import thi.hexa.authservice.domain.AuthService;

@RestController
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/login")
    String login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest.getUsername(), loginRequest.getPassword());
    }
}
