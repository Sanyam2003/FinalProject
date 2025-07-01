package com.tracker.controller;

import com.tracker.model.User;
import com.tracker.model.UtilityUsage;
import com.tracker.service.UserService;
import com.tracker.service.UtilityUsageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/usage")
public class UsageController {

    @Autowired
    private UtilityUsageService usageService;

    @Autowired
    private UserService userService;

    @GetMapping("/add")
    public String showForm(Model model) {
        model.addAttribute("usage", new UtilityUsage());
        return "usage_form";
    }

    @PostMapping("/add")
    public String saveUsage(@ModelAttribute UtilityUsage usage, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        usage.setUser(user);
        usageService.addUsage(user.getId(), usage);
        return "redirect:/usage/view";
    }

    @GetMapping("/view")
    public String viewList(Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        model.addAttribute("usageList", usageService.getUsageByUser(user.getId()));
        return "usage_list";
    }

    @GetMapping("/summary")
    public String viewSummary(Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        model.addAttribute("usages", usageService.getUsageByUser(user.getId()));
        return "usage_list";
    }
}
