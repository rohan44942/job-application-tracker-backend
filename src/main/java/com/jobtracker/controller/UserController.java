package com.jobtracker.controller;

import com.jobtracker.dto.MessageResponse;
import com.jobtracker.dto.user.ChangePasswordRequest;
import com.jobtracker.dto.user.UpdateProfileRequest;
import com.jobtracker.dto.user.UserResponse;
import com.jobtracker.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users/me")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public UserResponse profile() {
        return userService.profile();
    }

    @PutMapping
    public UserResponse update(@Valid @RequestBody UpdateProfileRequest request) {
        return userService.update(request);
    }

    @PutMapping("/password")
    public MessageResponse changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        return userService.changePassword(request);
    }
}
