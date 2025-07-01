package com.tracker.controller;

import com.tracker.model.User;
import com.tracker.service.UserService;
import com.tracker.service.UtilityUsageService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.Principal;

@Controller
public class HomeController {
    @Autowired
    private UserService userService;
    @Autowired
    private UtilityUsageService utilityUsageService;

//    //already in Auth Controller
//    @GetMapping({"/", "/login"})
//    public String loginPage() {
//        return "login";
//    }

//    @GetMapping("/summary")
//    public String summaryPage(Model model, Principal principal) {
//        if (principal != null) {
//            User user = userService.findByEmail(principal.getName());
//            model.addAttribute("usages", utilityUsageService.getUsageByUser(user.getId()));
//            return "usage_list";
//        }
//        return "redirect:/login";
//    }

    @GetMapping("/add-usage")
    public String addUsagePage(Model model) {
        model.addAttribute("usage", new com.tracker.model.UtilityUsage());
        return "add-usage";
    }

    @PostMapping("/add-usage")
    public String addUsageSubmit(@ModelAttribute("usage") com.tracker.model.UtilityUsage usage,
                                 Principal principal, Model model) {
        String email = principal.getName();
        User user = userService.findByEmail(email);
        if (user == null) {
            model.addAttribute("error", true);
            return "add-usage";
        }
        usage.setUser(user);
        try {
            utilityUsageService.addUsage(user.getId(), usage);
            model.addAttribute("success", true);
            model.addAttribute("usage", new com.tracker.model.UtilityUsage());
        } catch (Exception e) {
            model.addAttribute("error", true);
        }
        return "add-usage";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String email,
                               @RequestParam String password,
                               Model model) {
        try {
            userService.registerUser(new User(email, password));
            model.addAttribute("registerSuccess", true);
        } catch (Exception e) {
            model.addAttribute("registerError", true);
        }
        return "login";
    }
}
