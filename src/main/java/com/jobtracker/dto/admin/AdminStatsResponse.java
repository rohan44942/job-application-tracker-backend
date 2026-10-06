package com.jobtracker.dto.admin;

public record AdminStatsResponse(
        long totalUsers,
        long activeUsers,
        long totalApplications,
        long applicationsToday,
        long totalInterviews,
        long totalOffers
) {
}
