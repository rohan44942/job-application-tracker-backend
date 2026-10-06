package com.jobtracker.dto.dashboard;

public record MonthlyApplicationsResponse(
        String month,
        long applications
) {
}
