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

    public boolean validatePassword(int user_id, String password) {
        try {
            String url = userServiceUrl + "/checkpassword";
            ValidatePasswordRequest v = new ValidatePasswordRequest(user_id, password);
            ValidatePasswordReply vr = restTemplate.postForObject(url, v, ValidatePasswordReply.class);
            if (vr==null) {
                return false;
            }
            return vr.isSuccess();
        }catch (Exception e) {
            return false;
        }
    }

    public User getUser(int user_id) {
        try {
            String url = userServiceUrl+ "/" + user_id;
            GetUserResponse getUserResponse = restTemplate.getForObject(url, GetUserResponse.class);
            if (getUserResponse==null) {
                System.out.println("failed to get user");
                throw new Exception();
            }
            System.out.println(getUserResponse.getUser_id());
            return getUserResponse.toUser();
        }catch (Exception e) {
            return null;
        }

    }


}
