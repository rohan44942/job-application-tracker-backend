package com.jobtracker.service;

import com.jobtracker.dto.application.ApplicationRequest;
import com.jobtracker.dto.application.ApplicationResponse;
import com.jobtracker.dto.job.JobResponse;
import com.jobtracker.entity.ApplicationStatus;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class JobSearchService {

    private final ApplicationService applicationService;

    public JobSearchService(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    public List<JobResponse> search(String keyword, String location) {
        String safeKeyword = keyword == null || keyword.isBlank() ? "Java" : keyword;
        String safeLocation = location == null || location.isBlank() ? "Remote" : location;
        return List.of(new JobResponse("demo-1", "Demo Company", safeKeyword + " Developer", safeLocation, "https://example.com/jobs/demo-1", "DEMO"));
    }

    public ApplicationResponse save(String externalJobId) {
        JobResponse job = search("Java", "Remote").stream()
                .filter(item -> item.externalJobId().equals(externalJobId))
                .findFirst()
                .orElse(new JobResponse(externalJobId, "External Company", "External Job", "Remote", "https://example.com/jobs/" + externalJobId, "EXTERNAL"));
        ApplicationRequest request = new ApplicationRequest(
                job.companyName(),
                job.jobTitle(),
                job.location(),
                null,
                null,
                null,
                null,
                null,
                job.jobUrl(),
                job.source(),
                ApplicationStatus.SAVED,
                null,
                null,
                null,
                null
        );
        return applicationService.create(request);
    }
}
