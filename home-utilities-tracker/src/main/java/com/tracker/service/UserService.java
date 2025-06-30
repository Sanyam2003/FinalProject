package com.tracker.service;

import com.tracker.model.User;
import com.tracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User registerUser(User user) {
//        user.setPassword(passwordEncoder.encode(user.getPassword())); -> yaad nahi rakh paunga mai saare fake users ke passwords ko
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
//        return passwordEncoder.matches(password, user.getPassword());
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
        user.setPassword(newPassword); // Consider encoding in production
        userRepository.save(user);
        return true;
    }
}
