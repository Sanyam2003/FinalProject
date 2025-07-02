package com.tracker.controller;

import com.tracker.model.User;
import com.tracker.model.UtilityUsage;
import com.tracker.service.UserService;
import com.tracker.service.UtilityUsageService;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class ReportController {

    @Autowired
    private UtilityUsageService utilityUsageService;

    @Autowired
    private UserService userService;

    // ✅ View usage report
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

    // ✅ Handle edit/save action based on presence of input fields
    @PostMapping("/report")
    public String editOrUpdate(@RequestParam("editId") Long editId,
                               @RequestParam(required = false) String appliance,
                               @RequestParam(required = false) String utilityType,
                               @RequestParam(required = false) String subCategory,
                               @RequestParam(required = false) Double unitsUsed,
                               @RequestParam(required = false) Double usageCost,
                               @RequestParam(required = false) String date,
                               @RequestParam(required = false) String notes,
                               Principal principal, Model model) {

        if (appliance == null) {
            // 🟡 Only edit button clicked → return with form open
            return "redirect:/report?editId=" + editId;
        }

        // ✅ Otherwise perform update
        UtilityUsage usage = utilityUsageService.getUsageById(editId);
        if (usage == null || !usage.getUser().getEmail().equals(principal.getName())) {
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

    // ✅ Delete usage
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

    // Download as Excel
    @GetMapping("/report/download/excel")
    public void downloadExcel(Principal principal, HttpServletResponse response) throws IOException {
        String email = principal.getName();
        User user = userService.findByEmail(email);
        List<UtilityUsage> usages = utilityUsageService.getUsageByUser(user.getId());

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=utility_usage.xlsx");

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Usage");
        Row header = sheet.createRow(0);
        String[] columns = {"Appliance", "Utility Type", "Sub Category", "Units Used", "Usage Cost", "Date", "Notes"};
        for (int i = 0; i < columns.length; i++) header.createCell(i).setCellValue(columns[i]);
        int rowIdx = 1;
        for (UtilityUsage usage : usages) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(usage.getAppliance());
            row.createCell(1).setCellValue(usage.getUtilityType());
            row.createCell(2).setCellValue(usage.getSubCategory());
            row.createCell(3).setCellValue(usage.getUnitsUsed() != null ? usage.getUnitsUsed() : 0);
            row.createCell(4).setCellValue(usage.getUsageCost() != null ? usage.getUsageCost() : 0);
            row.createCell(5).setCellValue(usage.getDate() != null ? usage.getDate().toString() : "");
            row.createCell(6).setCellValue(usage.getNotes() != null ? usage.getNotes() : "");
        }
        workbook.write(response.getOutputStream());
        workbook.close();
    }

    // Download as PDF
    @GetMapping("/report/download/pdf")
    public void downloadPdf(Principal principal, HttpServletResponse response) throws IOException {
        String email = principal.getName();
        User user = userService.findByEmail(email);
        List<UtilityUsage> usages = utilityUsageService.getUsageByUser(user.getId());

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=utility_usage.pdf");
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();
        document.add(new Paragraph("Utility Usage Report"));
        document.add(new Paragraph(" "));
        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100);
        table.addCell("Appliance");
        table.addCell("Utility Type");
        table.addCell("Sub Category");
        table.addCell("Units Used");
        table.addCell("Usage Cost");
        table.addCell("Date");
        table.addCell("Notes");
        for (UtilityUsage usage : usages) {
            table.addCell(usage.getAppliance() != null ? usage.getAppliance() : "");
            table.addCell(usage.getUtilityType() != null ? usage.getUtilityType() : "");
            table.addCell(usage.getSubCategory() != null ? usage.getSubCategory() : "");
            table.addCell(usage.getUnitsUsed() != null ? usage.getUnitsUsed().toString() : "");
            table.addCell(usage.getUsageCost() != null ? usage.getUsageCost().toString() : "");
            table.addCell(usage.getDate() != null ? usage.getDate().toString() : "");
            table.addCell(usage.getNotes() != null ? usage.getNotes() : "");
        }
        document.add(table);
        document.close();
    }
}
