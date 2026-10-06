package com.jobtracker.controller;

import com.jobtracker.dto.interview.InterviewRequest;
import com.jobtracker.dto.interview.InterviewResponse;
import com.jobtracker.service.InterviewService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class InterviewController {

    private final InterviewService interviewService;

    public InterviewController(InterviewService interviewService) {
        this.interviewService = interviewService;
    }

    @PostMapping("/applications/{applicationId}/interviews")
    @ResponseStatus(HttpStatus.CREATED)
    public InterviewResponse create(@PathVariable Long applicationId, @Valid @RequestBody InterviewRequest request) {
        return interviewService.create(applicationId, request);
    }

    @GetMapping("/applications/{applicationId}/interviews")
    public List<InterviewResponse> getByApplication(@PathVariable Long applicationId) {
        return interviewService.getByApplication(applicationId);
    }

    @GetMapping("/interviews/{id}")
    public InterviewResponse get(@PathVariable Long id) {
        return interviewService.get(id);
    }

    @PutMapping("/interviews/{id}")
    public InterviewResponse update(@PathVariable Long id, @Valid @RequestBody InterviewRequest request) {
        return interviewService.update(id, request);
    }

    @DeleteMapping("/interviews/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        interviewService.delete(id);
    }
}
