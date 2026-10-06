package com.jobtracker.dto.dashboard;

public record SuccessRateResponse(
        double interviewRate,
        double offerRate,
        double rejectionRate,
        double interviewToOfferRate
) {
}
