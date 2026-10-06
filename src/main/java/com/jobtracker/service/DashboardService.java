package com.jobtracker.service;

import com.jobtracker.dto.dashboard.DashboardStatsResponse;
import com.jobtracker.dto.dashboard.SuccessRateResponse;
import com.jobtracker.entity.ApplicationStatus;
import com.jobtracker.entity.User;
import com.jobtracker.repository.JobApplicationRepository;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    private final JobApplicationRepository applicationRepository;
    private final CurrentUserService currentUserService;

    public DashboardService(JobApplicationRepository applicationRepository, CurrentUserService currentUserService) {
        this.applicationRepository = applicationRepository;
        this.currentUserService = currentUserService;
    }

    public DashboardStatsResponse stats() {
        User user = currentUserService.getCurrentUser();
        return new DashboardStatsResponse(
                applicationRepository.countByUser(user),
                applicationRepository.countByUserAndStatus(user, ApplicationStatus.SAVED),
                applicationRepository.countByUserAndStatus(user, ApplicationStatus.APPLIED),
                applicationRepository.countByUserAndStatus(user, ApplicationStatus.SCREENING),
                applicationRepository.countByUserAndStatus(user, ApplicationStatus.ASSESSMENT),
                applicationRepository.countByUserAndStatus(user, ApplicationStatus.INTERVIEW),
                applicationRepository.countByUserAndStatus(user, ApplicationStatus.OFFER),
                applicationRepository.countByUserAndStatus(user, ApplicationStatus.REJECTED),
                applicationRepository.countByUserAndStatus(user, ApplicationStatus.WITHDRAWN)
        );
    }

    public SuccessRateResponse successRate() {
        DashboardStatsResponse stats = stats();
        double total = stats.totalApplications();
        double interviewRate = rate(stats.interviews(), total);
        double offerRate = rate(stats.offers(), total);
        double rejectionRate = rate(stats.rejected(), total);
        double interviewToOfferRate = rate(stats.offers(), stats.interviews());
        return new SuccessRateResponse(interviewRate, offerRate, rejectionRate, interviewToOfferRate);
    }

    private double rate(double count, double total) {
        if (total == 0) {
            return 0;
        }
        return Math.round((count * 10000.0) / total) / 100.0;
    }
}
