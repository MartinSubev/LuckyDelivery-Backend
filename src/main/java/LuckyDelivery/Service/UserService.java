package LuckyDelivery.Service;

import LuckyDelivery.Model.User;
import LuckyDelivery.Repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(String username, String email, String password, String name, User.UserType type) {
        // Validations are now handled by the @Valid annotation on RegisterRequest object in the controller

        if (userRepository.existsByUsername(username)) {  // Use userRepository directly
            throw new IllegalArgumentException("Username already exists!");
        }

        if (userRepository.existsByEmail(email)) {  // Use userRepository directly
            throw new IllegalArgumentException("Email already exists!");
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(password)); // Encoding the password before storing
        user.setName(name);
        user.setType(type);

        return userRepository.save(user);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username); // Use userRepository directly
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email); // Use userRepository directly
    }

    public Optional<User> loginUser(String username, String password) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if(passwordEncoder.matches(password, user.getPasswordHash())){ // Comparing the encoded password
                return optionalUser;
            }
        }

        return Optional.empty();

    }
}