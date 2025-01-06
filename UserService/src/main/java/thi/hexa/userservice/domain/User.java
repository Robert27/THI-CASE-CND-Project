package thi.hexa.userservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class User {
    private Integer user_id;
    private String username;
    private String password_hash;
}
