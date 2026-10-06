package com.jobtracker.service;

import com.jobtracker.dto.user.UserResponse;
import com.jobtracker.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getLocation(),
                user.getYearsOfExperience(),
                user.getEnabled(),
                user.getRole()
        );
    }
}
