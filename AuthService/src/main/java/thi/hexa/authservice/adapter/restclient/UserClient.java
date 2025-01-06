package thi.hexa.authservice.adapter.restclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import thi.hexa.authservice.adapter.restclient.dto.ValidatePasswordReply;
import thi.hexa.authservice.adapter.restclient.dto.ValidatePasswordRequest;

@Service
public class UserClient {

    @Value("${userservice.url:http://localhost:8080/user}")
    private String userServiceUrl;

    private final RestTemplate restTemplate;

    public UserClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean validatePassword(int user_id, String password) {
        try {
            String url = userServiceUrl + "/checkpassword";
            ValidatePasswordRequest v = new ValidatePasswordRequest(user_id, password);
            ValidatePasswordReply vr = restTemplate.postForObject(url, v, ValidatePasswordReply.class);
            if (vr==null) {
                System.out.println("Invalid password");
                return false;
            }
            return vr.isSuccess();
        }catch (Exception e) {
            return false;
        }
    }


}
