package LuckyDelivery.Controller;

import LuckyDelivery.Model.User;
import LuckyDelivery.Repository.UserRepository;
import LuckyDelivery.Security.JwtUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {
        Optional<User> existing = userRepository.findByUsername(request.getUsername());
        if (existing.isPresent()) return "Username already taken";

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName());
        user.setEmail(request.getEmail());

        try {
            String type = request.getType() != null ? request.getType().toLowerCase() : "customer";
            user.setType(User.UserType.valueOf(type));
        } catch (IllegalArgumentException e) {
            return "Invalid user type. Must be: customer, employee, or supplier.";
        }

        userRepository.save(user);
        return "User registered successfully";
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        String token = jwtUtil.generateToken(request.getUsername());
        return new AuthResponse(token);
    }
}

@Data
class RegisterRequest {
    private String username;
    private String password;
    private String name;
    private String email;
    private String type; // "customer", "employee", or "supplier

}

@Data
class LoginRequest {
    private String username;
    private String password;
}

@Data
class AuthResponse {
    private final String token;
}
