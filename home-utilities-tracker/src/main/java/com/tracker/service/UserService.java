package com.tracker.service;

import com.tracker.model.User;
import com.tracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User registerUser(User user) {
        // Store password as plain text (not recommended for production)
        user.setRole("USER"); // default role
        return userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public boolean validateCredentials(String email, String password) {
        User user = findByEmail(email);
        if (user == null) {
            return false;
        }
        return password.equals(user.getPassword());
    }

    public boolean updateEmail(Long userId, String newEmail) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return false;
        user.setEmail(newEmail);
        userRepository.save(user);
        return true;
    }

    public boolean updatePassword(Long userId, String newPassword) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return false;
        user.setPassword(newPassword); // Store as plain text
        userRepository.save(user);
        return true;
    }
}
