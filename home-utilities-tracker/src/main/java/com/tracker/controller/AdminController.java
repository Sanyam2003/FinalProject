package com.tracker.controller;

import com.tracker.model.UtilityUsage;
import com.tracker.repository.UtilityUsageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UtilityUsageRepository usageRepo;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all-usage")
    public List<UtilityUsage> getAllUsage() {
        return usageRepo.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete-usage/{id}")
    public String deleteUsage(@PathVariable Long id) {
        usageRepo.deleteById(id);
        return "Usage record deleted.";
    }
}
