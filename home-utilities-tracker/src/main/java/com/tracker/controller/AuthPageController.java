package com.tracker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthPageController {

    @GetMapping("/register")
    public String showRegisterForm() {
        return "register"; // looks for templates/register.html
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; // looks for templates/login.html
    }
}

