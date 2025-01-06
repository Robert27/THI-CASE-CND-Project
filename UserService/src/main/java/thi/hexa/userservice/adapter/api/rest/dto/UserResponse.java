package thi.hexa.userservice.adapter.api.rest.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponse {
    private int user_id;
    private String username;
}
