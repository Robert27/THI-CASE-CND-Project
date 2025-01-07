package thi.hexa.authservice.adapter.api.rest.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginRequest {
    private int user_id;
    private String password;
}
