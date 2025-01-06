package thi.hexa.authservice.domain;




public interface AuthService {

    String login(String username, String password);
    String decode(String token);
    String encode(String token);
    String generateToken(User user);

}
