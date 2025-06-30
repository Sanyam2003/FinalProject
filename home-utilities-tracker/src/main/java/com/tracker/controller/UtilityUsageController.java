package com.tracker.controller;

import com.tracker.model.UtilityUsage;
import com.tracker.service.UtilityUsageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/usage")
public class UtilityUsageController {

    @Autowired
    private UtilityUsageService utilityUsageService;

    @Autowired
    private UtilityUsageService usageService;

    @PostMapping("/add")
    public ResponseEntity<UtilityUsage> addUsage(@RequestParam Long userId, @RequestBody UtilityUsage usage) {
        UtilityUsage saved = usageService.addUsage(userId, usage);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/user/{userId}")
    public List<UtilityUsage> getUsage(@PathVariable Long userId) {
        return usageService.getUsageByUser(userId);
    }

    @PutMapping("/update/{usageId}")
    public ResponseEntity<?> updateUsage(
            @PathVariable Long usageId,
            @RequestBody UtilityUsage updatedUsage,
            Principal principal) {

        // ✅ 1. Get email of currently logged-in user
        String loggedInEmail = principal.getName();

        // ✅ 2. Check if user is authorized to update this record (admin or owner)
        UtilityUsage existingUsage = utilityUsageService.getUsageById(usageId);
        String ownerEmail = existingUsage.getUser().getEmail();

        // ✅ 3. Allow if same user or admin
        if (!loggedInEmail.equals(ownerEmail) && !loggedInEmail.equals("admin@example.com")) {
            return ResponseEntity.status(403).body("Access Denied: You cannot update others' data.");
        }

        // ✅ 4. Perform update
        UtilityUsage updated = utilityUsageService.updateUsageById(usageId, updatedUsage);
        return ResponseEntity.ok(updated);
    }


    @DeleteMapping("/delete/{usageId}")
    public ResponseEntity<Void> deleteUsage(@PathVariable Long usageId) {
        usageService.deleteUsage(usageId);
        return ResponseEntity.noContent().build();
    }
}
