package com.jobtracker.controller;

import com.jobtracker.dto.admin.AdminStatsResponse;
import com.jobtracker.dto.user.UserResponse;
import com.jobtracker.service.AdminService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/users")
    public List<UserResponse> users() {
        return adminService.users();
    }

    @GetMapping("/users/{id}")
    public UserResponse user(@PathVariable Long id) {
        return adminService.user(id);
    }

    @PatchMapping("/users/{id}/disable")
    public UserResponse disable(@PathVariable Long id) {
        return adminService.disable(id);
    }

    @PatchMapping("/users/{id}/enable")
    public UserResponse enable(@PathVariable Long id) {
        return adminService.enable(id);
    }

    @GetMapping("/stats")
    public AdminStatsResponse stats() {
        return adminService.stats();
    }
}
