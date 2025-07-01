package com.tracker.controller;

import com.tracker.model.User;
import com.tracker.model.UtilityUsage;
import com.tracker.service.UserService;
import com.tracker.service.UtilityUsageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/usage")
public class UtilityUsageController {

    @Autowired
    private UtilityUsageService utilityUsageService;

    @Autowired
    private UserService userService;

    // ✅ Add usage (logged-in user only)
    @PostMapping("/add")
    public ResponseEntity<UtilityUsage> addUsage(@RequestBody UtilityUsage usage, Principal principal) {
        String email = principal.getName();
        User user = userService.findByEmail(email);

        usage.setUser(user);
        UtilityUsage saved = utilityUsageService.addUsage(user.getId(), usage);
        return ResponseEntity.ok(saved);
    }

    // ✅ Update usage (logged-in user only)
    @PutMapping("/update/{usageId}")
    public ResponseEntity<?> updateUsage(@PathVariable Long usageId, @RequestBody UtilityUsage updatedUsage, Principal principal) {
        String loggedInEmail = principal.getName();
        UtilityUsage existingUsage = utilityUsageService.getUsageById(usageId);
        if (existingUsage == null) {
            return ResponseEntity.notFound().build();
        }

        String ownerEmail = existingUsage.getUser().getEmail();
        if (!loggedInEmail.equals(ownerEmail)) {
            return ResponseEntity.status(403).body("Access Denied: You cannot update others' data.");
        }

        UtilityUsage updated = utilityUsageService.updateUsageById(usageId, updatedUsage);
        return ResponseEntity.ok(updated);
    }

    // ✅ Delete usage (logged-in user only)
    @DeleteMapping("/delete/{usageId}")
    public ResponseEntity<?> deleteUsage(@PathVariable Long usageId, Principal principal) {
        UtilityUsage usage = utilityUsageService.getUsageById(usageId);
        if (usage == null) {
            return ResponseEntity.notFound().build();
        }

        String loggedInEmail = principal.getName();
        String ownerEmail = usage.getUser().getEmail();

        if (!loggedInEmail.equals(ownerEmail)) {
            return ResponseEntity.status(403).body("Access Denied: Not your record");
        }

        utilityUsageService.deleteUsage(usageId);
        return ResponseEntity.ok("Deleted successfully");
    }

    // ✅ View your own usage
    @GetMapping("/my-usage")
    public ResponseEntity<List<UtilityUsage>> getMyUsage(Authentication authentication) {
        String email = authentication.getName();
        User user = userService.findByEmail(email);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        List<UtilityUsage> usageList = utilityUsageService.getUsageByUser(user.getId());
        return ResponseEntity.ok(usageList);
    }
}
