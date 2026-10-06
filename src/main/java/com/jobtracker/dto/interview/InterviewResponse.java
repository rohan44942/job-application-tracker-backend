package com.jobtracker.dto.interview;

import com.jobtracker.entity.InterviewStatus;
import com.jobtracker.entity.InterviewType;
import java.time.LocalDateTime;

public record InterviewResponse(
        Long id,
        Long applicationId,
        Integer round,
        InterviewType type,
        LocalDateTime scheduledAt,
        Integer durationMinutes,
        String interviewerName,
        String meetingUrl,
        InterviewStatus status,
        String feedback,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
