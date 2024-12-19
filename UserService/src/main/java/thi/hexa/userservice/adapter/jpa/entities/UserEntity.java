package thi.hexa.userservice.adapter.jpa.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import thi.hexa.userservice.domain.User;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

    public UserEntity(User user) {
        this.passwordHash = user.getPassword_hash();
        this.username = user.getUsername();
    }

    @Id
    private String username;

    private String passwordHash;

    public User toUser(){
        return new User(this.username, this.passwordHash);
    }

}
