package com.jobtracker.service;

import com.jobtracker.dto.PageResponse;
import com.jobtracker.dto.application.ApplicationRequest;
import com.jobtracker.dto.application.ApplicationResponse;
import com.jobtracker.dto.application.StatusHistoryResponse;
import com.jobtracker.dto.application.StatusUpdateRequest;
import com.jobtracker.entity.ApplicationStatus;
import com.jobtracker.entity.JobApplication;
import com.jobtracker.entity.JobType;
import com.jobtracker.entity.StatusHistory;
import com.jobtracker.entity.User;
import com.jobtracker.entity.WorkMode;
import com.jobtracker.exception.BadRequestException;
import com.jobtracker.exception.ResourceNotFoundException;
import com.jobtracker.repository.JobApplicationRepository;
import com.jobtracker.repository.StatusHistoryRepository;
import java.time.LocalDate;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ApplicationService {

    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of("appliedDate", "createdAt", "companyName", "status");

    private final JobApplicationRepository applicationRepository;
    private final StatusHistoryRepository statusHistoryRepository;
    private final CurrentUserService currentUserService;
    private final ApplicationMapper mapper;
    private final AuditService auditService;

    public ApplicationService(JobApplicationRepository applicationRepository, StatusHistoryRepository statusHistoryRepository, CurrentUserService currentUserService, ApplicationMapper mapper, AuditService auditService) {
        this.applicationRepository = applicationRepository;
        this.statusHistoryRepository = statusHistoryRepository;
        this.currentUserService = currentUserService;
        this.mapper = mapper;
        this.auditService = auditService;
    }

    @Transactional
    public ApplicationResponse create(ApplicationRequest request) {
        User user = currentUserService.getCurrentUser();
        validateSalary(request);
        JobApplication application = new JobApplication();
        application.setUser(user);
        mapper.updateEntity(application, request);
        JobApplication saved = applicationRepository.save(application);
        saveHistory(saved, null, saved.getStatus(), "Application created");
        auditService.record(user, "CREATE_APPLICATION", "JobApplication", saved.getId(), saved.getCompanyName());
        return mapper.toResponse(saved);
    }

    public PageResponse<ApplicationResponse> getAll(int page, int size, String sort) {
        User user = currentUserService.getCurrentUser();
        Page<JobApplication> result = applicationRepository.findByUser(user, pageable(page, size, sort));
        return toPageResponse(result);
    }

    public ApplicationResponse get(Long id) {
        return mapper.toResponse(findOwned(id));
    }

    @Transactional
    public ApplicationResponse update(Long id, ApplicationRequest request) {
        validateSalary(request);
        JobApplication application = findOwned(id);
        ApplicationStatus oldStatus = application.getStatus();
        mapper.updateEntity(application, request);
        JobApplication saved = applicationRepository.save(application);
        if (oldStatus != saved.getStatus()) {
            saveHistory(saved, oldStatus, saved.getStatus(), "Status changed during update");
        }
        auditService.record(currentUserService.getCurrentUser(), "UPDATE_APPLICATION", "JobApplication", saved.getId(), saved.getCompanyName());
        return mapper.toResponse(saved);
    }

    @Transactional
    public void delete(Long id) {
        JobApplication application = findOwned(id);
        applicationRepository.delete(application);
        auditService.record(currentUserService.getCurrentUser(), "DELETE_APPLICATION", "JobApplication", id, application.getCompanyName());
    }

    public PageResponse<ApplicationResponse> search(String company, String jobTitle, ApplicationStatus status, String location, WorkMode workMode, JobType jobType, LocalDate fromDate, LocalDate toDate, int page, int size, String sort) {
        User user = currentUserService.getCurrentUser();
        Page<JobApplication> result = applicationRepository.search(user, company, jobTitle, status, location, workMode, jobType, fromDate, toDate, pageable(page, size, sort));
        return toPageResponse(result);
    }

    @Transactional
    public ApplicationResponse changeStatus(Long id, StatusUpdateRequest request) {
        JobApplication application = findOwned(id);
        ApplicationStatus oldStatus = application.getStatus();
        application.setStatus(request.status());
        JobApplication saved = applicationRepository.save(application);
        saveHistory(saved, oldStatus, request.status(), request.comment());
        auditService.record(currentUserService.getCurrentUser(), "CHANGE_STATUS", "JobApplication", saved.getId(), oldStatus + " to " + request.status());
        return mapper.toResponse(saved);
    }

    public java.util.List<StatusHistoryResponse> history(Long id) {
        JobApplication application = findOwned(id);
        return statusHistoryRepository.findByApplicationOrderByChangedAtDesc(application).stream()
                .map(item -> new StatusHistoryResponse(item.getOldStatus(), item.getNewStatus(), item.getChangedAt(), item.getComment()))
                .toList();
    }

    public JobApplication findOwned(Long id) {
        User user = currentUserService.getCurrentUser();
        return applicationRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));
    }

    private void saveHistory(JobApplication application, ApplicationStatus oldStatus, ApplicationStatus newStatus, String comment) {
        StatusHistory history = new StatusHistory();
        history.setApplication(application);
        history.setOldStatus(oldStatus);
        history.setNewStatus(newStatus);
        history.setComment(comment);
        statusHistoryRepository.save(history);
    }

    private void validateSalary(ApplicationRequest request) {
        if (request.salaryMin() != null && request.salaryMax() != null && request.salaryMin().compareTo(request.salaryMax()) > 0) {
            throw new BadRequestException("Minimum salary cannot be greater than maximum salary");
        }
    }

    private Pageable pageable(int page, int size, String sort) {
        int safeSize = Math.min(Math.max(size, 1), 100);
        Sort springSort = Sort.by(Sort.Direction.DESC, "createdAt");
        if (sort != null && !sort.isBlank()) {
            String[] parts = sort.split(",");
            String property = parts[0].trim();
            if (!ALLOWED_SORT_FIELDS.contains(property)) {
                throw new BadRequestException("Sort field must be appliedDate, createdAt, companyName or status");
            }
            Sort.Direction direction = parts.length > 1 && parts[1].trim().equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
            springSort = Sort.by(direction, property);
        }
        return PageRequest.of(Math.max(page, 0), safeSize, springSort);
    }

    private PageResponse<ApplicationResponse> toPageResponse(Page<JobApplication> result) {
        return new PageResponse<>(result.getContent().stream().map(mapper::toResponse).toList(), result.getNumber(), result.getSize(), result.getTotalElements(), result.getTotalPages());
    }
}
