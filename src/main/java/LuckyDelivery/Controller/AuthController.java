package LuckyDelivery.Controller;

import java.util.Optional;
import LuckyDelivery.Service.UserService;
import LuckyDelivery.Model.User;
import LuckyDelivery.Model.DTO.LoginRequest;
import LuckyDelivery.Model.DTO.RegisterRequest;
import LuckyDelivery.Security.JwtTokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService; // Сервизен слой
    private final BCryptPasswordEncoder passwordEncoder; // За хеширане на пароли
    private final JwtTokenProvider jwtTokenProvider; // За създаване на токен (ако използваме JWT)

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequest registerRequest) {
        // Проверка дали има такъв потребител
        if (userService.existsByUsername(registerRequest.getUsername())) {
            return ResponseEntity.badRequest().body("Username is already taken!");
        }

        if (userService.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity.badRequest().body("Email is already in use!");
        }

        // Създаване на нов User обект
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setName(registerRequest.getName());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword())); // Хеширане на паролата
        user.setType(registerRequest.getType());

        // Запазване на потребителя
        userService.saveUser(user);

        return ResponseEntity.status(201).body("User registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginRequest loginRequest) {
        Optional<User> optionalUser = userService.loginUser(loginRequest.getUsername(), loginRequest.getPassword());
        if (optionalUser.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid username or password!");
        }

        User user = optionalUser.get();
        String token = jwtTokenProvider.generateToken(user.getUsername());

        return ResponseEntity.ok(token);
    }
}