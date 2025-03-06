package com.example.SpringBootBasic.service;

import com.example.SpringBootBasic.model.User;
import com.example.SpringBootBasic.repository.UserRepository;
import com.example.SpringBootBasic.security.JwtUtil;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    // Register a new user
    public Map<String, String> registerUser(String name, String email, String password) {
        Map<String, String> response = new HashMap<>();
        
        if (userRepository.findByEmail(email).isPresent()) {
            response.put("error", "Email already registered!");
            return response;
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        // ✅ Ensure this works properly
        User newUser = new User(name, email, hashedPassword);
        userRepository.save(newUser);

        response.put("message", "User registered successfully!");
        return response;
    }

    // Login and return JWT token in JSON
    public Map<String, String> loginUser(String email, String password) {
        Map<String, String> response = new HashMap<>();

        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent() && BCrypt.checkpw(password, user.get().getPassword())) {
            response.put("token", jwtUtil.generateToken(email));
            return response;
        }

        response.put("error", "Invalid email or password!");
        return response;
    }

    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
