package thi.hexa.userservice.adapter.api.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckPasswordRequest {
    private String username;
    private String password;
}
