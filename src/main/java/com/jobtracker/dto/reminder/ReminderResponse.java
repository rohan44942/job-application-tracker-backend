package com.jobtracker.dto.reminder;

import com.jobtracker.entity.ReminderStatus;
import java.time.LocalDateTime;

public record ReminderResponse(
        Long id,
        Long applicationId,
        String title,
        String message,
        LocalDateTime remindAt,
        ReminderStatus status,
        LocalDateTime createdAt
) {
}
