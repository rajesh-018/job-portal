package com.rajesh.jobportal.controller;

import com.rajesh.jobportal.dto.JobApplicationRequestDto;
import com.rajesh.jobportal.dto.JobApplicationResponseDto;
import com.rajesh.jobportal.dto.JobApplicationStatusRequestDto;
import com.rajesh.jobportal.entity.Job;
import com.rajesh.jobportal.entity.JobApplication;
import com.rajesh.jobportal.entity.User;
import com.rajesh.jobportal.repository.JobRepository;
import com.rajesh.jobportal.repository.UserRepository;
import com.rajesh.jobportal.service.JobApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class JobApplicationController {

    private final JobApplicationService jobApplicationService;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;

    public JobApplicationController(
            JobApplicationService jobApplicationService,
            UserRepository userRepository,
            JobRepository jobRepository) {

        this.jobApplicationService = jobApplicationService;
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
    }

    @PostMapping
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public ResponseEntity<JobApplicationResponseDto> applyForJob(
            @Valid @RequestBody JobApplicationRequestDto request,
            Authentication authentication) {

        User jobSeeker =
                userRepository.findByEmail(authentication.getName())
                        .orElseThrow(() ->
                                new RuntimeException("User not found"));

        Job job =
                jobRepository.findById(request.getJobId())
                        .orElseThrow(() ->
                                new RuntimeException("Job not found"));

        JobApplication application =
                new JobApplication();

        application.setJob(job);
        application.setJobSeeker(jobSeeker);

        JobApplication savedApplication =
                jobApplicationService.applyForJob(application);

        JobApplicationResponseDto response =
                new JobApplicationResponseDto();

        response.setId(savedApplication.getId());
        response.setJobId(savedApplication.getJob().getId());
        response.setJobTitle(savedApplication.getJob().getTitle());
        response.setJobSeekerId(
                savedApplication.getJobSeeker().getId()
        );
        response.setJobSeekerName(
                savedApplication.getJobSeeker().getName()
        );
        response.setStatus(savedApplication.getStatus());
        response.setAppliedAt(savedApplication.getAppliedAt());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public ResponseEntity<List<JobApplicationResponseDto>>
    getMyApplications(Authentication authentication) {

        User jobSeeker =
                userRepository.findByEmail(authentication.getName())
                        .orElseThrow(() ->
                                new RuntimeException("User not found"));

        return ResponseEntity.ok(
                jobApplicationService.getMyApplications(
                        jobSeeker.getId()
                )
        );
    }

    @GetMapping("/recruiter")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<List<JobApplicationResponseDto>>
    getApplicationsForRecruiter(
            Authentication authentication) {

        User recruiter =
                userRepository.findByEmail(authentication.getName())
                        .orElseThrow(() ->
                                new RuntimeException("User not found"));

        return ResponseEntity.ok(
                jobApplicationService.getApplicationsForRecruiter(
                        recruiter.getId()
                )
        );
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<JobApplicationResponseDto>
    updateApplicationStatus(
            @PathVariable Long id,
            @Valid @RequestBody JobApplicationStatusRequestDto request,
            Authentication authentication) {

        User recruiter =
                userRepository.findByEmail(authentication.getName())
                        .orElseThrow(() ->
                                new RuntimeException("User not found"));

        JobApplicationResponseDto response =
                jobApplicationService.updateApplicationStatus(
                        id,
                        request,
                        recruiter.getId()
                );

        return ResponseEntity.ok(response);
    }
}