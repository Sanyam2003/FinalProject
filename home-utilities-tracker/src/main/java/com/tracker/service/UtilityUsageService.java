package com.tracker.service;

import com.tracker.model.UtilityUsage;
import com.tracker.model.User;
import com.tracker.repository.UtilityUsageRepository;
import com.tracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UtilityUsageService {

    @Autowired
    private UtilityUsageRepository usageRepo;

    @Autowired
    private UserRepository userRepo;

    public UtilityUsage addUsage(Long userId, UtilityUsage usage) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        usage.setUser(user);
        return usageRepo.save(usage);
    }

    public List<UtilityUsage> getUsageByUser(Long userId) {
        return usageRepo.findByUserId(userId);
    }

    public UtilityUsage updateUsage(Long usageId, UtilityUsage updatedUsage) {
        UtilityUsage existing = usageRepo.findById(usageId)
                .orElseThrow(() -> new RuntimeException("Usage record not found"));
        existing.setUtilityType(updatedUsage.getUtilityType());
        existing.setUnitsUsed(updatedUsage.getUnitsUsed());
        existing.setUsageCost(updatedUsage.getUsageCost());
        existing.setDate(updatedUsage.getDate());
        existing.setAppliance(updatedUsage.getAppliance());
        existing.setSubCategory(updatedUsage.getSubCategory());
        existing.setNotes(updatedUsage.getNotes());
        // Add other fields as needed
        return usageRepo.save(existing);
    }

    public void deleteUsage(Long usageId) {
        if (!usageRepo.existsById(usageId)) {
            throw new RuntimeException("Usage record not found");
        }
        usageRepo.deleteById(usageId);
    }
}
