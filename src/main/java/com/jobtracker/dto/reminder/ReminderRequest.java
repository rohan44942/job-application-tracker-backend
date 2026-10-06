package com.jobtracker.dto.reminder;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record ReminderRequest(
        Long applicationId,
        @NotBlank String title,
        @NotBlank String message,
        @NotNull @Future LocalDateTime remindAt
) {
}
