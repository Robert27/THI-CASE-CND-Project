package thi.hexa.userservice.ports.outgoing;

import thi.hexa.userservice.domain.User;

import java.util.Optional;

public interface UserRepository {
    boolean save(User user);
    boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);
    boolean update(User user);
    boolean deleteByUsername(String name);
}
