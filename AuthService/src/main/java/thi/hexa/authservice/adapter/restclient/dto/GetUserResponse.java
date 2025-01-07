package thi.hexa.authservice.adapter.restclient.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import thi.hexa.authservice.domain.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetUserResponse {
    private int user_id;
    private String username;

    public User toUser() {
        return new User(user_id, username);
    }
}
