package com.tracker.controller;

import com.tracker.model.User;
import com.tracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
public class ProfileController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public String showProfilePage() {
        return "profile";
    }

    @PostMapping("/profile/update-email")
    public String updateEmail(@RequestParam("newEmail") String newEmail, Principal principal) {
        String currentEmail = principal.getName();
        User user = userService.findByEmail(currentEmail);
        userService.updateEmail(user.getId(), newEmail);
        return "redirect:/logout"; // force re-login after email change
    }

    @PostMapping("/profile/update-password")
    public String updatePassword(@RequestParam("newPassword") String newPassword, Principal principal) {
        String currentEmail = principal.getName();
        User user = userService.findByEmail(currentEmail);
        userService.updatePassword(user.getId(), newPassword);
        return "redirect:/logout"; // force re-login after password change
    }
}
