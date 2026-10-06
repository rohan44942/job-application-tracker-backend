package com.jobtracker.dto.interview;

import com.jobtracker.entity.InterviewStatus;
import com.jobtracker.entity.InterviewType;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;
import java.time.LocalDateTime;

public record InterviewRequest(
        @NotNull @Min(1) Integer round,
        @NotNull InterviewType type,
        @NotNull @FutureOrPresent LocalDateTime scheduledAt,
        @Min(1) Integer durationMinutes,
        String interviewerName,
        @URL String meetingUrl,
        InterviewStatus status,
        String feedback
) {
}
