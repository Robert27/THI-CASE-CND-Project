package thi.hexa.userservice.adapter.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import thi.hexa.userservice.adapter.jpa.entities.UserEntity;
import thi.hexa.userservice.domain.User;
import thi.hexa.userservice.ports.outgoing.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserRepositoryImpl implements UserRepository {

    @Autowired
    private JpaUserRepository jpaUserRepository;

    @Override
    public User save(User user) {
        if(jpaUserRepository.existsByUsername(user.getUsername())) {
            return null;
        }
        UserEntity userEntity = new UserEntity(user);
        try {
            UserEntity u = jpaUserRepository.save(userEntity);
            return u.toUser();
        } catch (IllegalArgumentException | OptimisticLockingFailureException e) {
            return null;
        }
    }

    @Override
    public boolean existsByUserID(int user_id) {
        return jpaUserRepository.existsById(user_id);
    }

    @Override
    public Optional<User> findByUserID(int user_id) {
        Optional<UserEntity> oue = jpaUserRepository.findById(user_id);
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
        if (existsByUserID(user.getUser_id())) {
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
    public boolean deleteByUserID(int user_id) {
        try {
            jpaUserRepository.deleteById(user_id);
        }catch (IllegalArgumentException e){
            return false;
        }
        return true;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        Optional<UserEntity> oue =jpaUserRepository.findByUsername(username);
        if (oue.isEmpty()) {
            return Optional.empty();
        } else {
            UserEntity ue =oue.get();
            User u = ue.toUser();
            return Optional.of(u);
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<UserEntity> ue =jpaUserRepository.findAll();
        List<User> ul = new ArrayList<>();
        for (UserEntity userEntity : ue) {
            ul.add(userEntity.toUser());
        }
        return ul;
    }
}
