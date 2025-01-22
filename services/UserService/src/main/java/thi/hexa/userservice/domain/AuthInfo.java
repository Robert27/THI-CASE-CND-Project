package thi.hexa.userservice.domain;


import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AuthInfo {

    private String username;
    private Integer userId;
    private boolean authservice;


}
