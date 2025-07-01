package com.tracker.controller;

import com.tracker.model.User;
import com.tracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            userService.registerUser(user);
            return ResponseEntity.ok("User registered successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestParam String email, @RequestParam String password) {
        boolean isValid = userService.validateCredentials(email, password);
        if (isValid) {
            return ResponseEntity.ok("Login successful!");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password!");
        }
    }

    @PutMapping("/update-email")
    public ResponseEntity<?> updateEmail(@RequestParam Long userId, @RequestParam String newEmail) {
        boolean updated = userService.updateEmail(userId, newEmail);
        return updated ?
                ResponseEntity.ok("Email updated successfully!") :
                ResponseEntity.badRequest().body("Failed to update email.");
    }

    @PutMapping("/update-password")
    public ResponseEntity<?> updatePassword(@RequestParam Long userId, @RequestParam String newPassword) {
        boolean updated = userService.updatePassword(userId, newPassword);
        return updated ?
                ResponseEntity.ok("Password updated successfully!") :
                ResponseEntity.badRequest().body("Failed to update password.");
    }
}
