package com.jobtracker.dto.job;

public record JobResponse(
        String externalJobId,
        String companyName,
        String jobTitle,
        String location,
        String jobUrl,
        String source
) {
}
