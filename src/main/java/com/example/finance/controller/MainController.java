package com.example.finance.controller;
import com.example.finance.dto.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.finance.User;
import com.example.finance.UserDao;

import jakarta.servlet.http.HttpServletResponse;

import com.example.finance.JwtTokenUtil;


import jakarta.servlet.http.Cookie;  // Correct import for Jakarta Cookie
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;  // For Spring Boot 2.4+ and Jakarta EE

@RestController
public class MainController {


    // Endpoint for the home page
    @GetMapping("/")
    public String home() {
        return "Welcome to the Spring Boot Application!";
    }

    // Example of another endpoint
    @GetMapping("/greeting")
    public String getGreeting() {
        return "Hello, World!";
    }
    
    @GetMapping("/createStudent")
    public String createUserNew() {
        UserDao userDao = new UserDao();
        User user = new User("john_doe", "password123@");
        userDao.saveUser(user);
        return "success";
    }

    @RequestMapping(value = "/signin", method = RequestMethod.POST)
    public ResponseEntity<String> userSignIn(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        try {
            String username = loginRequest.getUsername();
            String password = loginRequest.getPassword();
            
            // Use Hibernate to retrieve the user by username
            User user = new User();
            UserDao userDao = new UserDao();
            user = userDao.getUser(username,password);
            
            if (user != null) {
                // If authentication is successful, generate the token
                String token = JwtTokenUtil.generateToken(username);
                setJwtCookie(response, token);
                // Create a JSON response containing the token
                return ResponseEntity.ok().body("{\"token\":\"" + token + "\"}");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }
        } catch (Exception e) {
            // Log the exception (you can use a logger here)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }

    private void setJwtCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("jwt", token);
        cookie.setHttpOnly(true);  // Makes the cookie inaccessible to JavaScript
        cookie.setSecure(true);    // Ensures the cookie is sent over HTTPS
        cookie.setPath("/");       // Makes the cookie available for the entire domain
        cookie.setMaxAge(60 * 60 * 24);  // Set expiration time (e.g., 1 day)
    
        // Manually set the SameSite attribute (workaround for older versions)
        response.addCookie(cookie);
        
        // Manually add the SameSite attribute via the "Set-Cookie" header
        response.setHeader("Set-Cookie", "jwt=" + token + "; HttpOnly; Secure; Path=/; Max-Age=86400; SameSite=Strict");
    }

    // You can add more endpoints here
}
