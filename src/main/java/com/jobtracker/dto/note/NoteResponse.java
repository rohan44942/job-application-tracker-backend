package com.jobtracker.dto.note;

import java.time.LocalDateTime;

public record NoteResponse(
        Long id,
        Long applicationId,
        String title,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
