package com.jobtracker.controller;

import com.jobtracker.dto.dashboard.DashboardStatsResponse;
import com.jobtracker.dto.dashboard.MonthlyApplicationsResponse;
import com.jobtracker.dto.dashboard.SuccessRateResponse;
import com.jobtracker.service.DashboardService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/stats")
    public DashboardStatsResponse stats() {
        return dashboardService.stats();
    }

    @GetMapping("/success-rate")
    public SuccessRateResponse successRate() {
        return dashboardService.successRate();
    }

    @GetMapping("/monthly")
    public List<MonthlyApplicationsResponse> monthly() {
        return dashboardService.monthly();
    }
}
