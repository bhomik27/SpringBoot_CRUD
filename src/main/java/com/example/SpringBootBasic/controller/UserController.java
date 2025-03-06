package com.example.SpringBootBasic.controller;

import com.example.SpringBootBasic.service.UserService;
import com.example.SpringBootBasic.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    // Register API - Returns JSON response
    @PostMapping("/register")
    public Map<String, String> registerUser(@RequestBody Map<String, String> request) {
        return userService.registerUser(request.get("name"), request.get("email"), request.get("password"));
    }

    // Login API - Returns JWT Token or error message in JSON
    @PostMapping("/login")
    public Map<String, String> loginUser(@RequestBody Map<String, String> request) {
        return userService.loginUser(request.get("email"), request.get("password"));
    }

    // Home API - Requires JWT in Header
    @GetMapping("/home")
    public Map<String, String> home(@RequestHeader("Authorization") String token) {
        if (jwtUtil.validateToken(token)) {
            return Map.of("message", "Welcome to the Home Page, " + jwtUtil.extractEmail(token) + "!");
        }
        return Map.of("error", "Invalid token!");
    }
}
