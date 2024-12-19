package thi.hexa.userservice.application;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thi.hexa.userservice.domain.User;
import thi.hexa.userservice.domain.UserService;
import thi.hexa.userservice.ports.outgoing.UserRepository;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User createUser(String username, String password) {
        if (userRepository.existsByUsername(username)) {
            return null;
        }
        User u = new User(username, hashPassword(password));
        userRepository.save(u);
        return u;
    }

    @Override
    public User getUser(String id) {
        return userRepository.findByUsername(id).orElse(null);
    }

    @Override
    public boolean deleteUser(String id) {
        return userRepository.deleteByUsername(id);
    }

    @Override
    public boolean changePassword(String id, String oldPassword, String newPassword) {
        User u = getUser(id);
        if(!verifyPassword(u, oldPassword)) {
            return false;
        } else {
            u.setPassword_hash(hashPassword(newPassword));
            updateUser(u);
            return true;
        }
    }

    @Override
    public boolean verifyPassword(User user, String password) {
        return BCrypt.checkpw(password, user.getPassword_hash());
    }

    @Override
    public boolean updateUser(User user) {
        return userRepository.update(user);
    }

    public static String hashPassword(String password) {
        // Generate a salt
        String salt = BCrypt.gensalt();
        // Hash the password with the salt
        return BCrypt.hashpw(password, salt);
    }
}
