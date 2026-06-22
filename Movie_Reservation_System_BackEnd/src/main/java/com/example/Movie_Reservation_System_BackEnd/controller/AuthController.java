package com.example.Movie_Reservation_System_BackEnd.controller;

import com.example.Movie_Reservation_System_BackEnd.dto.AuthRequest;
import com.example.Movie_Reservation_System_BackEnd.entity.User;
import com.example.Movie_Reservation_System_BackEnd.entity.Role;
import com.example.Movie_Reservation_System_BackEnd.repository.UserRepository;
import com.example.Movie_Reservation_System_BackEnd.config.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    // Best Practice: Constructor Dependency Injection
    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody AuthRequest request) {
        // Business logic rule validation check
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists!");
        }

        // Create a new domain model entity mapping to MySQL
        User user = new User();
        user.setUsername(request.getUsername());
        
        // Securely hashing the incoming password payload before storing it
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        
        // Defaulting newly registered accounts to standard user clearance status
        user.setRole(Role.USER); 

        userRepository.save(user);
        
        // Returns HTTP 201 Created for REST compliance
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));

        // Match raw text password input parameter against encoded database secure hash
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }

        // Generate stateless cryptographically signed JWT payload token
        String token = jwtService.generateToken(user.getUsername(), user.getRole().name());
        
        // Return JSON token bundle back to client
        return ResponseEntity.ok(Map.of("token", token));
    }
}