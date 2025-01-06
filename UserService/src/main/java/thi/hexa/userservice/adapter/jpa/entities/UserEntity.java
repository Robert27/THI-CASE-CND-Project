package thi.hexa.userservice.adapter.jpa.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import thi.hexa.userservice.domain.User;

@Entity
@Table(name="users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

    public UserEntity(User user) {
        this.user_id = user.getUser_id();
        this.passwordHash = user.getPassword_hash();
        this.username = user.getUsername();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Integer user_id;

    @Column(name="username")
    private String username;

    @Column(name="passwordHash")
    private String passwordHash;

    public User toUser(){
        return new User(this.user_id, this.username, this.passwordHash);
    }

}
