package com.joeburin.event_photo_sharing.controller;

import com.joeburin.event_photo_sharing.dto.AuthRequest;
import com.joeburin.event_photo_sharing.entity.User;
import com.joeburin.event_photo_sharing.security.JwtUtil;
import com.joeburin.event_photo_sharing.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String role = userDetails.getAuthorities().iterator().next().getAuthority();
        String jwt = jwtUtil.generateToken(userDetails.getUsername(), role);
        return ResponseEntity.ok(jwt);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AuthRequest authRequest) {
        try {
            userService.registerNewUser(
                    authRequest.getUsername(),
                    authRequest.getPassword(),
                    authRequest.getRole()
            );
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        }
    }

    @GetMapping("/test-password")
    public ResponseEntity<String> testPassword(@RequestParam String password) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        User user = userService.findByUsername("organizer")
                .orElseThrow(() -> new RuntimeException("User not found"));
        String hash = user.getPassword();
        boolean matches = encoder.matches(password, hash);
        return ResponseEntity.ok("Password: " + password + "\nHash: " + hash + "\nMatches: " + matches);
    }

    @GetMapping("/generate-hash")
    public ResponseEntity<String> generateHash(@RequestParam String password) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = encoder.encode(password);
        return ResponseEntity.ok("Password: " + password + "\nHash: " + hash);
    }
}
