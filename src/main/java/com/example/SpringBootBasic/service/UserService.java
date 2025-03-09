package com.example.SpringBootBasic.service;

import com.example.SpringBootBasic.model.User;
import com.example.SpringBootBasic.repository.UserRepository;
import com.example.SpringBootBasic.security.JwtUtil;
import com.example.SpringBootBasic.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public Map<String, String> registerUser(String name, String email, String password) {
        Map<String, String> response = new HashMap<>();

        if (userRepository.findByEmail(email).isPresent()) {
            response.put("error", "Email already registered!");
            return response;
        }

        User newUser = new User(name, email, PasswordUtil.hashPassword(password));
        userRepository.save(newUser);

        response.put("message", "User registered successfully!");
        return response;
    }

    public Map<String, String> loginUser(String email, String password) {
        Map<String, String> response = new HashMap<>();
    
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent() && PasswordUtil.checkPassword(password, user.get().getPassword())) {
            response.put("token", jwtUtil.generateToken(email));
            response.put("name", user.get().getName());  // ✅ Include username in response
            return response;
        }
    
        response.put("error", "Invalid email or password!");
        return response;
    }
    

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    public String getUserNameByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(User::getName)
                .orElse("User"); // ✅ Returns the user's actual name or "User" if not found
    }
    
}


