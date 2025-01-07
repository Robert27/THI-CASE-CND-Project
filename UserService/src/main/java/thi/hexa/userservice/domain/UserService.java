package thi.hexa.userservice.domain;

public interface UserService {
    User createUser(String username, String password);
    User getUser(int userId);
    boolean deleteUser(int userId);
    boolean changePassword(int userId, String oldPassword, String newPassword);
    boolean verifyPassword(User user, String password);
    boolean updateUser(User user);

}
