package com.tracker.repository;

import com.tracker.model.UtilityUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UtilityUsageRepository extends JpaRepository<UtilityUsage, Long> {
    List<UtilityUsage> findByUserId(Long userId);
}
