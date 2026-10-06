package com.jobtracker.service;

import com.jobtracker.dto.interview.InterviewRequest;
import com.jobtracker.dto.interview.InterviewResponse;
import com.jobtracker.entity.Interview;
import com.jobtracker.entity.InterviewStatus;
import com.jobtracker.entity.JobApplication;
import com.jobtracker.exception.ResourceNotFoundException;
import com.jobtracker.repository.InterviewRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InterviewService {

    private final InterviewRepository interviewRepository;
    private final ApplicationService applicationService;
    private final CurrentUserService currentUserService;
    private final AuditService auditService;

    public InterviewService(InterviewRepository interviewRepository, ApplicationService applicationService, CurrentUserService currentUserService, AuditService auditService) {
        this.interviewRepository = interviewRepository;
        this.applicationService = applicationService;
        this.currentUserService = currentUserService;
        this.auditService = auditService;
    }

    @Transactional
    public InterviewResponse create(Long applicationId, InterviewRequest request) {
        JobApplication application = applicationService.findOwned(applicationId);
        Interview interview = new Interview();
        interview.setApplication(application);
        updateFields(interview, request);
        Interview saved = interviewRepository.save(interview);
        auditService.record(currentUserService.getCurrentUser(), "CREATE_INTERVIEW", "Interview", saved.getId(), application.getCompanyName());
        return toResponse(saved);
    }

    public List<InterviewResponse> getByApplication(Long applicationId) {
        JobApplication application = applicationService.findOwned(applicationId);
        return interviewRepository.findByApplicationOrderByScheduledAtAsc(application).stream().map(this::toResponse).toList();
    }

    public InterviewResponse get(Long id) {
        return toResponse(findOwned(id));
    }

    @Transactional
    public InterviewResponse update(Long id, InterviewRequest request) {
        Interview interview = findOwned(id);
        updateFields(interview, request);
        return toResponse(interviewRepository.save(interview));
    }

    @Transactional
    public void delete(Long id) {
        Interview interview = findOwned(id);
        interviewRepository.delete(interview);
        auditService.record(currentUserService.getCurrentUser(), "DELETE_INTERVIEW", "Interview", id, null);
    }

    private Interview findOwned(Long id) {
        Long userId = currentUserService.getCurrentUser().getId();
        return interviewRepository.findByIdAndApplicationUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Interview not found"));
    }

    private void updateFields(Interview interview, InterviewRequest request) {
        interview.setRoundNumber(request.round());
        interview.setType(request.type());
        interview.setScheduledAt(request.scheduledAt());
        interview.setDurationMinutes(request.durationMinutes());
        interview.setInterviewerName(request.interviewerName());
        interview.setMeetingUrl(request.meetingUrl());
        interview.setStatus(request.status() == null ? InterviewStatus.SCHEDULED : request.status());
        interview.setFeedback(request.feedback());
    }

    private InterviewResponse toResponse(Interview interview) {
        return new InterviewResponse(
                interview.getId(),
                interview.getApplication().getId(),
                interview.getRoundNumber(),
                interview.getType(),
                interview.getScheduledAt(),
                interview.getDurationMinutes(),
                interview.getInterviewerName(),
                interview.getMeetingUrl(),
                interview.getStatus(),
                interview.getFeedback(),
                interview.getCreatedAt(),
                interview.getUpdatedAt()
        );
    }
}
