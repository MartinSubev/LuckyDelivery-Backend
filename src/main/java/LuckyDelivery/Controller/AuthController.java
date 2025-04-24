package LuckyDelivery.Controller;

import LuckyDelivery.Model.DTO.LoginRequest;
import LuckyDelivery.Model.DTO.RegisterRequest;
import LuckyDelivery.Model.DTO.AuthResponse;
import LuckyDelivery.Model.User;
import LuckyDelivery.Security.JwtUtil;
import LuckyDelivery.Service.UserService;
import LuckyDelivery.Service.UserService.UserAlreadyExistsException;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    public AuthController(UserService userService, AuthenticationManager authenticationManager, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());

            User registeredUser = userService.registerUser(
                    registerRequest.getUsername(),
                    registerRequest.getEmail(),
                    encodedPassword,
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
            System.out.println(">>> LOGIN ATTEMPT for: " + request.getUsername());

            // Print out the password you are checking (BE CAREFUL in production)
            System.out.println(">>> Checking password: " + request.getPassword());

            // Attempt to authenticate
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            System.out.println(">>> AUTHENTICATION SUCCESS");

            // Generate JWT token if authentication is successful
            String token = jwtUtil.generateToken(request.getUsername());
            System.out.println(">>> JWT TOKEN GENERATED: " + token);

            return ResponseEntity.ok(new AuthResponse(token));
        } catch (AuthenticationException e) {
            System.out.println(">>> AUTHENTICATION FAILED: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse(null));
        }
    }

}