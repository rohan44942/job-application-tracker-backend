package com.jobtracker.dto.application;

import com.jobtracker.entity.ApplicationStatus;
import jakarta.validation.constraints.NotNull;

public record StatusUpdateRequest(
        @NotNull ApplicationStatus status,
        String comment
) {
}
