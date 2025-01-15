package thi.hexa.userservice.adapter.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import thi.hexa.userservice.adapter.jpa.entities.UserEntity;

import java.util.Optional;

public interface JpaUserRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findByUsername(String username);
    boolean existsByUsername(String username);
}
