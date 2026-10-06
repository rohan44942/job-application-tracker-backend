package com.jobtracker.dto.application;

import com.jobtracker.entity.ApplicationStatus;
import com.jobtracker.entity.JobType;
import com.jobtracker.entity.WorkMode;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ApplicationResponse(
        Long id,
        String companyName,
        String jobTitle,
        String location,
        JobType jobType,
        WorkMode workMode,
        BigDecimal salaryMin,
        BigDecimal salaryMax,
        String currency,
        String jobUrl,
        String source,
        ApplicationStatus status,
        LocalDate appliedDate,
        LocalDate deadline,
        String description,
        String notes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
