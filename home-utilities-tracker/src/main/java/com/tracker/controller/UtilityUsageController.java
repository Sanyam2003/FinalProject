package com.tracker.controller;

import com.tracker.model.UtilityUsage;
import com.tracker.service.UtilityUsageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usage")
public class UtilityUsageController {

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
    public ResponseEntity<UtilityUsage> updateUsage(@PathVariable Long usageId, @RequestBody UtilityUsage usage) {
        UtilityUsage updated = usageService.updateUsage(usageId, usage);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete/{usageId}")
    public ResponseEntity<Void> deleteUsage(@PathVariable Long usageId) {
        usageService.deleteUsage(usageId);
        return ResponseEntity.noContent().build();
    }
}
