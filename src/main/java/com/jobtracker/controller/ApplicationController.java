package com.jobtracker.controller;

import com.jobtracker.dto.PageResponse;
import com.jobtracker.dto.application.ApplicationRequest;
import com.jobtracker.dto.application.ApplicationResponse;
import com.jobtracker.dto.application.StatusHistoryResponse;
import com.jobtracker.dto.application.StatusUpdateRequest;
import com.jobtracker.entity.ApplicationStatus;
import com.jobtracker.entity.JobType;
import com.jobtracker.entity.WorkMode;
import com.jobtracker.service.ApplicationService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApplicationResponse create(@Valid @RequestBody ApplicationRequest request) {
        return applicationService.create(request);
    }

    @GetMapping
    public PageResponse<ApplicationResponse> getAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String sort) {
        return applicationService.getAll(page, size, sort);
    }

    @GetMapping("/{id}")
    public ApplicationResponse get(@PathVariable Long id) {
        return applicationService.get(id);
    }

    @PutMapping("/{id}")
    public ApplicationResponse update(@PathVariable Long id, @Valid @RequestBody ApplicationRequest request) {
        return applicationService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        applicationService.delete(id);
    }

    @GetMapping("/search")
    public PageResponse<ApplicationResponse> search(
            @RequestParam(required = false) String company,
            @RequestParam(required = false) String jobTitle,
            @RequestParam(required = false) ApplicationStatus status,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) WorkMode workMode,
            @RequestParam(required = false) JobType jobType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sort) {
        return applicationService.search(company, jobTitle, status, location, workMode, jobType, fromDate, toDate, page, size, sort);
    }

    @PatchMapping("/{id}/status")
    public ApplicationResponse changeStatus(@PathVariable Long id, @Valid @RequestBody StatusUpdateRequest request) {
        return applicationService.changeStatus(id, request);
    }

    @GetMapping("/{id}/history")
    public List<StatusHistoryResponse> history(@PathVariable Long id) {
        return applicationService.history(id);
    }
}
