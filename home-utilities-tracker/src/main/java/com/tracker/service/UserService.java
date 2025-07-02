package com.tracker.service;

import com.tracker.model.User;
import com.tracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(User user) {
        user.setRole("USER"); // default role
        user.setPassword(passwordEncoder.encode(user.getPassword()));
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
        return passwordEncoder.matches(password, user.getPassword());
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
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return true;
    }
}
