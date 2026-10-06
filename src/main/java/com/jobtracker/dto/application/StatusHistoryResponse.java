package com.jobtracker.dto.application;

import com.jobtracker.entity.ApplicationStatus;
import java.time.LocalDateTime;

public record StatusHistoryResponse(
        ApplicationStatus oldStatus,
        ApplicationStatus newStatus,
        LocalDateTime changedAt,
        String comment
) {
}
