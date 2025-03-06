package com.example.SpringBootBasic.controller;

import com.example.SpringBootBasic.model.User;
import com.example.SpringBootBasic.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")  // Base URL mapping
public class AppController {

    @Autowired
    private UserService userService;

    // Home page (serves static home.html)
    @GetMapping
    public String home() {
        return "redirect:/home.html";  // Redirects to static file in /static/
    }

    // Index page (serves index.html from Thymeleaf templates)
    @GetMapping("/homePage")
    public String homePage(Map<String, Object> model) {
        List<User> users = userService.getAllUsers();
        model.put("users", users);
        return "index";  // Must exist in /templates/index.html
    }

    // Register page (Thymeleaf template or static)
    @GetMapping("/register")
    public String register() {
        return "register";  // Assumes register.html exists in /templates/ or /static/
    }

    // Login page (Thymeleaf template or static)
    @GetMapping("/login")
    public String login() {
        return "login";  // Assumes login.html exists in /templates/ or /static/
    }
}
