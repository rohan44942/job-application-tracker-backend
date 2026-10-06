package com.jobtracker.dto.application;

import com.jobtracker.entity.ApplicationStatus;
import com.jobtracker.entity.JobType;
import com.jobtracker.entity.WorkMode;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;
import java.math.BigDecimal;
import java.time.LocalDate;

public record ApplicationRequest(
        @NotBlank String companyName,
        @NotBlank String jobTitle,
        String location,
        JobType jobType,
        WorkMode workMode,
        @DecimalMin("0.0") BigDecimal salaryMin,
        @DecimalMin("0.0") BigDecimal salaryMax,
        String currency,
        @URL String jobUrl,
        String source,
        ApplicationStatus status,
        LocalDate appliedDate,
        LocalDate deadline,
        @Size(max = 4000) String description,
        @Size(max = 4000) String notes
) {
}
