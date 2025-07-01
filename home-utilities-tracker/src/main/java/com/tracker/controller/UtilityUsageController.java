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
    private UserService userService;  // ✅ You missed this, which is why userService is red

    // ✅ Add usage entry by userId
    @PostMapping("/add")
    public ResponseEntity<UtilityUsage> addUsage(@RequestParam Long userId, @RequestBody UtilityUsage usage) {
        UtilityUsage saved = utilityUsageService.addUsage(userId, usage);
        return ResponseEntity.ok(saved);
    }

    // View all usage data by specific user
    @GetMapping("/user/{userId}")
    public List<UtilityUsage> getUsage(@PathVariable Long userId) {
        return utilityUsageService.getUsageByUser(userId);
    }

    // Update usage by ID (user only)
    @PutMapping("/update/{usageId}")
    public ResponseEntity<?> updateUsage(
            @PathVariable Long usageId,
            @RequestBody UtilityUsage updatedUsage,
            Principal principal) {

        String loggedInEmail = principal.getName();
        UtilityUsage existingUsage = utilityUsageService.getUsageById(usageId);
        String ownerEmail = existingUsage.getUser().getEmail();

        if (!loggedInEmail.equals(ownerEmail)) {
            return ResponseEntity.status(403).body("Access Denied: You cannot update others' data.");
        }

        UtilityUsage updated = utilityUsageService.updateUsageById(usageId, updatedUsage);
        return ResponseEntity.ok(updated);
    }

    // Delete usage
    @DeleteMapping("/delete/{usageId}")
    public ResponseEntity<Void> deleteUsage(@PathVariable Long usageId) {
        utilityUsageService.deleteUsage(usageId);
        return ResponseEntity.noContent().build();
    }

    // ✅ Logged-in user can see only their entries
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
