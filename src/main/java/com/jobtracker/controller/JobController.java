package com.jobtracker.controller;

import com.jobtracker.dto.job.JobResponse;
import com.jobtracker.dto.application.ApplicationResponse;
import com.jobtracker.service.JobSearchService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/jobs")
public class JobController {

    private final JobSearchService jobSearchService;

    public JobController(JobSearchService jobSearchService) {
        this.jobSearchService = jobSearchService;
    }

    @GetMapping("/search")
    public List<JobResponse> search(@RequestParam(required = false) String keyword, @RequestParam(required = false) String location) {
        return jobSearchService.search(keyword, location);
    }

    @PostMapping("/{externalJobId}/save")
    @ResponseStatus(HttpStatus.CREATED)
    public ApplicationResponse save(@PathVariable String externalJobId) {
        return jobSearchService.save(externalJobId);
    }
}
