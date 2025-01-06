package thi.hexa.userservice.ports.outgoing;

import thi.hexa.userservice.domain.User;

import java.util.Optional;

public interface UserRepository {
    User save(User user);
    boolean existsByUserID(int user_id);
    Optional<User> findByUserID(int user_id);
    boolean update(User user);
    boolean deleteByUserID(int user_id);
}
