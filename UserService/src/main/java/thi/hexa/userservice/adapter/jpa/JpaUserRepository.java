package thi.hexa.userservice.adapter.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import thi.hexa.userservice.adapter.jpa.entities.UserEntity;

public interface JpaUserRepository extends JpaRepository<UserEntity, String> {
}
