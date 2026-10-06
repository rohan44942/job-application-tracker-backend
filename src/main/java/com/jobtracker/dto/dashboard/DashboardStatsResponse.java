package com.jobtracker.dto.dashboard;

public record DashboardStatsResponse(
        long totalApplications,
        long saved,
        long applied,
        long screening,
        long assessment,
        long interviews,
        long offers,
        long rejected,
        long withdrawn
) {
}
