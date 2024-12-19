package thi.hexa.userservice.domain;

public interface UserService {
    User createUser(String username, String password);
    User getUser(String username);
    boolean deleteUser(String username);
    boolean changePassword(String username, String oldPassword, String newPassword);
    boolean verifyPassword(User user, String password);
    boolean updateUser(User user);

}
