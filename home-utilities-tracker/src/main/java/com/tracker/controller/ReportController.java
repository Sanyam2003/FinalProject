package com.tracker.controller;

import com.tracker.model.User;
import com.tracker.model.UtilityUsage;
import com.tracker.service.UserService;
import com.tracker.service.UtilityUsageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Controller
public class ReportController {
    @Autowired
    private UtilityUsageService utilityUsageService;
    @Autowired
    private UserService userService;

    @GetMapping("/report")
    public String reportPage(@RequestParam(value = "editId", required = false) Long editId,
                             Model model, Principal principal) {
        String email = principal.getName();
        User user = userService.findByEmail(email);
        List<UtilityUsage> usages = utilityUsageService.getUsageByUser(user.getId());
        model.addAttribute("usages", usages);
        model.addAttribute("editId", editId);
        return "report";
    }

    @PostMapping("/report")
    public String editOrUpdate(@RequestParam(value = "editId") Long editId,
                               @RequestParam(required = false) String appliance,
                               @RequestParam(required = false) String utilityType,
                               @RequestParam(required = false) String subCategory,
                               @RequestParam(required = false) Double unitsUsed,
                               @RequestParam(required = false) Double usageCost,
                               @RequestParam(required = false) String date,
                               @RequestParam(required = false) String notes,
                               Principal principal, Model model) {
        // If only editId is present, show edit mode
        if (appliance == null) {
            return "redirect:/report?editId=" + editId;
        }
        // Otherwise, update the record
        UtilityUsage usage = utilityUsageService.getUsageById(editId);
        if (usage == null) {
            model.addAttribute("error", true);
            return "redirect:/report";
        }
        String email = principal.getName();
        if (!usage.getUser().getEmail().equals(email)) {
            model.addAttribute("error", true);
            return "redirect:/report";
        }
        usage.setAppliance(appliance);
        usage.setUtilityType(utilityType);
        usage.setSubCategory(subCategory);
        usage.setUnitsUsed(unitsUsed);
        usage.setUsageCost(usageCost);
        usage.setDate(LocalDate.parse(date));
        usage.setNotes(notes);
        utilityUsageService.updateUsageById(editId, usage);
        model.addAttribute("success", true);
        return "redirect:/report";
    }

    @PostMapping("/report/delete")
    public String deleteUsage(@RequestParam("deleteId") Long deleteId, Principal principal, Model model) {
        UtilityUsage usage = utilityUsageService.getUsageById(deleteId);
        if (usage != null && usage.getUser().getEmail().equals(principal.getName())) {
            utilityUsageService.deleteUsage(deleteId);
            model.addAttribute("success", true);
        } else {
            model.addAttribute("error", true);
        }
        return "redirect:/report";
    }
}
