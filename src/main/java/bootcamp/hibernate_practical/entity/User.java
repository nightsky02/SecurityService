package bootcamp.hibernate_practical.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="users")
@NoArgsConstructor
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String passwordHash;
    private String role;
    private boolean isAvailable;

    public User(String username, String passwordHash, String role){
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
        this.isAvailable = true;
    }
}
