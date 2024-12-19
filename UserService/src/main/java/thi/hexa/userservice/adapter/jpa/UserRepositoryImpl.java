package thi.hexa.userservice.adapter.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import thi.hexa.userservice.adapter.jpa.entities.UserEntity;
import thi.hexa.userservice.domain.User;
import thi.hexa.userservice.ports.outgoing.UserRepository;

import java.util.Optional;

@Service
public class UserRepositoryImpl implements UserRepository {

    @Autowired
    private JpaUserRepository jpaUserRepository;

    @Override
    public boolean save(User user) {
        UserEntity userEntity = new UserEntity(user);
        try {
            jpaUserRepository.save(userEntity);
        } catch (IllegalArgumentException | OptimisticLockingFailureException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean existsByUsername(String username) {
        return jpaUserRepository.existsById(username);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        Optional<UserEntity> oue = jpaUserRepository.findById(username);
        if (oue.isEmpty()) {
            return Optional.empty();
        } else {
            UserEntity ue =oue.get();
            User u = ue.toUser();
            return Optional.of(u);
        }
    }

    @Override
    public boolean update(User user) {
        if (existsByUsername(user.getUsername())) {
            try {
                jpaUserRepository.save(new UserEntity(user));
            } catch (IllegalArgumentException | OptimisticLockingFailureException e) {
                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteByUsername(String name) {
        try {
            jpaUserRepository.deleteById(name);
        }catch (IllegalArgumentException e){
            return false;
        }
        return true;
    }
}
