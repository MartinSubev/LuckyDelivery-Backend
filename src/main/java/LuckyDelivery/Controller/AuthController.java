package LuckyDelivery.Controller;

import LuckyDelivery.Model.DTO.LoginRequest;
import LuckyDelivery.Model.DTO.RegisterRequest;
import LuckyDelivery.Model.User;
import LuckyDelivery.Service.UserService;
import LuckyDelivery.Service.UserService.UserAlreadyExistsException; // Import the custom exception
import jakarta.validation.Valid;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            User registeredUser = userService.registerUser(
                    registerRequest.getUsername(),
                    registerRequest.getEmail(),
                    registerRequest.getPassword(),
                    registerRequest.getName(),
                    registerRequest.getType()
            );
            return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.badRequest().body(e.getMessage()); // Return 400 Bad Request
        }
    }


    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        Optional<User> optionalUser = userService.loginUser(loginRequest.getUsername(), loginRequest.getPassword());

        if (optionalUser.isPresent()) {
            String mockJwtToken = "your-jwt-token-here"; // Replace with actual JWT generation
            return ResponseEntity.ok(mockJwtToken);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }

    }
}