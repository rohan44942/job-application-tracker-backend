package com.jobtracker.service;

import com.jobtracker.dto.application.ApplicationRequest;
import com.jobtracker.dto.application.ApplicationResponse;
import com.jobtracker.entity.ApplicationStatus;
import com.jobtracker.entity.JobApplication;
import org.springframework.stereotype.Component;

@Component
public class ApplicationMapper {

    public void updateEntity(JobApplication application, ApplicationRequest request) {
        application.setCompanyName(request.companyName());
        application.setJobTitle(request.jobTitle());
        application.setLocation(request.location());
        application.setJobType(request.jobType());
        application.setWorkMode(request.workMode());
        application.setSalaryMin(request.salaryMin());
        application.setSalaryMax(request.salaryMax());
        application.setCurrency(request.currency());
        application.setJobUrl(request.jobUrl());
        application.setSource(request.source());
        application.setStatus(request.status() == null ? ApplicationStatus.SAVED : request.status());
        application.setAppliedDate(request.appliedDate());
        application.setDeadline(request.deadline());
        application.setDescription(request.description());
        application.setNotes(request.notes());
    }

    public ApplicationResponse toResponse(JobApplication application) {
        return new ApplicationResponse(
                application.getId(),
                application.getCompanyName(),
                application.getJobTitle(),
                application.getLocation(),
                application.getJobType(),
                application.getWorkMode(),
                application.getSalaryMin(),
                application.getSalaryMax(),
                application.getCurrency(),
                application.getJobUrl(),
                application.getSource(),
                application.getStatus(),
                application.getAppliedDate(),
                application.getDeadline(),
                application.getDescription(),
                application.getNotes(),
                application.getCreatedAt(),
                application.getUpdatedAt()
        );
    }
}
