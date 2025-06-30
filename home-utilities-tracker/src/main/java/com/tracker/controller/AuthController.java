package com.tracker.controller;

import com.tracker.model.User;
import com.tracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController // ✅ Replaces @Controller + @ResponseBody on every method
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String registerUser(@RequestBody User user) {
        userService.registerUser(user);
        return "User registered successfully!";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String email,
                            @RequestParam String password) {
        boolean isValid = userService.validateCredentials(email, password);
        if (isValid) {
            return "Login successful!";
        } else {
            return "Invalid email or password!";
        }
    }

    @PutMapping("/update-email")
    public String updateEmail(@RequestParam Long userId, @RequestParam String newEmail) {
        boolean updated = userService.updateEmail(userId, newEmail);
        return updated ? "Email updated successfully!" : "Failed to update email.";
    }

    @PutMapping("/update-password")
    public String updatePassword(@RequestParam Long userId, @RequestParam String newPassword) {
        boolean updated = userService.updatePassword(userId, newPassword);
        return updated ? "Password updated successfully!" : "Failed to update password.";
    }
}
