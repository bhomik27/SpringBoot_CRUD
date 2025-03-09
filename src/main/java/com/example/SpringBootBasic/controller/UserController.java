package com.example.SpringBootBasic.controller;

import com.example.SpringBootBasic.service.UserService;
import com.example.SpringBootBasic.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Register a new user.
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody Map<String, String> request) {
        if (!request.containsKey("name") || !request.containsKey("email") || !request.containsKey("password")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Missing required fields"));
        }

        Map<String, String> response = userService.registerUser(request.get("name"), request.get("email"), request.get("password"));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Login user and return token + username.
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody Map<String, String> request) {
        if (!request.containsKey("email") || !request.containsKey("password")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Missing required fields"));
        }

        Map<String, String> response = userService.loginUser(request.get("email"), request.get("password"));

        if (response.containsKey("token")) {
            // ✅ Ensure username is included in the response
            response.put("name", userService.getUserNameByEmail(request.get("email")));
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    /**
     * Fetch user home data with authorization.
     */
    @GetMapping("/home")
    public ResponseEntity<Map<String, String>> home(@RequestHeader("Authorization") String token) {
        if (jwtUtil.validateToken(token)) {
            String email = jwtUtil.extractEmail(token);
            String userName = userService.getUserNameByEmail(email);

            return ResponseEntity.ok(Map.of("message", "Welcome, " + userName + "!"));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid token!"));
    }
}
