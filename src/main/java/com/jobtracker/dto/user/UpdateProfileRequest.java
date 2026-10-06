package com.jobtracker.dto.user;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record UpdateProfileRequest(
        @NotBlank String name,
        String phone,
        String location,
        @Min(0) Integer yearsOfExperience
) {
}
