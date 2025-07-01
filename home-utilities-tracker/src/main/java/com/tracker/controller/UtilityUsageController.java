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
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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
    @PutMapping("/usage/update/{id}")
    public String updateUsage(@PathVariable Long id,
                              @RequestParam Map<String, String> params,
                              Principal principal) {

        String email = principal.getName();
        UtilityUsage existing = utilityUsageService.getUsageById(id);

        if (!existing.getUser().getEmail().equals(email)) {
            return "redirect:/report?error=unauthorized";
        }

        // Update fields manually from param map
        existing.setAppliance(params.get("appliance_" + id));
        existing.setUtilityType(params.get("utilityType_" + id));
        existing.setSubCategory(params.get("subCategory_" + id));
        existing.setUnitsUsed(Double.parseDouble(params.get("unitsUsed_" + id)));
        existing.setUsageCost(Double.parseDouble(params.get("usageCost_" + id)));
        existing.setDate(LocalDate.parse(params.get("date_" + id)));
        existing.setNotes(params.get("notes_" + id));

        utilityUsageService.updateUsageById(id, existing);
        return "redirect:/report?success=updated";
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
