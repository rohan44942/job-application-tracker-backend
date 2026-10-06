package com.jobtracker.dto.note;

import jakarta.validation.constraints.NotBlank;

public record NoteRequest(
        @NotBlank String title,
        @NotBlank String content
) {
}
