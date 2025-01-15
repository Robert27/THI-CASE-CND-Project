package thi.hexa.authservice.adapter.api.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginReply {

    private String token;
    private boolean success;
    private String Reason;

}
