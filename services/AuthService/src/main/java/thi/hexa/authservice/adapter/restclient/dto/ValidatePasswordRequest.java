package thi.hexa.authservice.adapter.restclient.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidatePasswordRequest {
    private String username;
    private String password;
}