package com.tracker.controller;

import com.tracker.model.UtilityUsage;
import com.tracker.repository.UtilityUsageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UtilityUsageRepository usageRepo;

    @GetMapping("/usage")
    public List<UtilityUsage> getUsage() {
        return usageRepo.findAll();
    }

    @DeleteMapping("/delete-usage/{id}")
    public String deleteUsage(@PathVariable Long id) {
        usageRepo.deleteById(id);
        return "Usage record deleted.";
    }
}
