package com.tracker.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
@Getter
@Entity
@Table(name = "utility_usage")
public class UtilityUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "utility_type")
    private String utilityType;

    @Column(name = "appliance")
    private String appliance;

    @Column(name = "units_used")
    private Double unitsUsed;

    @Column(name = "usage_cost")
    private Double usageCost;

    @Column(name = "sub_category")
    private String subCategory;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "notes")
    private String notes;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Getters & Setters

    public void setId(Long id) {
        this.id = id;
    }

    public void setUtilityType(String utilityType) {
        this.utilityType = utilityType;
    }

    public void setAppliance(String appliance) {
        this.appliance = appliance;
    }

    public void setUnitsUsed(Double unitsUsed) {
        this.unitsUsed = unitsUsed;
    }

    public void setUsageCost(Double usageCost) {
        this.usageCost = usageCost;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
