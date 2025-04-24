package LuckyDelivery.Controller;

import LuckyDelivery.Model.DTO.LoginRequest;
import LuckyDelivery.Model.DTO.RegisterRequest;
import LuckyDelivery.Model.DTO.AuthResponse;
import org.springframework.security.core.AuthenticationException;
import LuckyDelivery.Model.User;
import LuckyDelivery.Security.JwtUtil; // Import JwtUtil
import LuckyDelivery.Service.UserService;
import LuckyDelivery.Service.UserService.UserAlreadyExistsException;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager; // Inject AuthenticationManager
    @Autowired
    private JwtUtil jwtUtil; // Inject JwtUtil


    @Autowired
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
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            String token = jwtUtil.generateToken(request.getUsername());
            return ResponseEntity.ok(new AuthResponse(token)); // Return 200 OK with token

        } catch (AuthenticationException e) {
            System.err.println("Authentication failed: " + e.getMessage()); // Log the detailed message!
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse(null)); // Return 401 Unauthorized
        }
    }

}