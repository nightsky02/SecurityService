package bootcamp.hibernate_practical.service;

import bootcamp.hibernate_practical.entity.User;
import bootcamp.hibernate_practical.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository repository;


    public UserService(UserRepository repository, PasswordEncoder encoder) {
        repository.save(new User(
                "user",
                encoder.encode("userpassword"),
                "USER"));

        repository.save(new User(
                "admin",
                encoder.encode("adminpassword"),
                "ADMIN"));

        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(username.trim().isEmpty()){
            throw new UsernameNotFoundException("Username must not be empty!");
        }

        User user = this.repository.findByUsername(username);

        if(user == null){
            throw new UsernameNotFoundException("No user found");
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPasswordHash())
                .roles(user.getRole())
                .disabled(!user.isAvailable())
                .build();
    }
}
