package thi.hexa.authservice.adapter.restclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import thi.hexa.authservice.adapter.restclient.dto.GetUserResponse;
import thi.hexa.authservice.adapter.restclient.dto.ValidatePasswordReply;
import thi.hexa.authservice.adapter.restclient.dto.ValidatePasswordRequest;
import thi.hexa.authservice.domain.User;

@Service
public class UserClient {

    @Value("${userservice.url:http://localhost:8080/user}")
    private String userServiceUrl;

    private final RestTemplate restTemplate;

    public UserClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean validatePassword(String username, String password) {
        try {
            String url = userServiceUrl + "/checkpassword";
            System.out.println(url);
            ValidatePasswordRequest v = new ValidatePasswordRequest(username, password);
            ValidatePasswordReply vr = restTemplate.postForObject(url, v, ValidatePasswordReply.class);
            if (vr==null) {
                return false;
            }
            return vr.isSuccess();
        }catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public User getUser(String username) {
        try {
            String url = userServiceUrl+ "/" + username;
            GetUserResponse getUserResponse = restTemplate.getForObject(url, GetUserResponse.class);
            if (getUserResponse==null) {
                System.out.println("failed to get user");
                throw new Exception();
            }
            return getUserResponse.toUser();
        }catch (Exception e) {
            return null;
        }

    }


}
