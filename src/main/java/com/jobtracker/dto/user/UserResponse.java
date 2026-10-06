package com.jobtracker.dto.user;

import com.jobtracker.entity.UserRole;

public record UserResponse(
        Long id,
        String name,
        String email,
        String phone,
        String location,
        Integer yearsOfExperience,
        Boolean enabled,
        UserRole role
) {
}
