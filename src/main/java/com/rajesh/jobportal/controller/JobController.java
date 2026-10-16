package com.rajesh.jobportal.controller;
import com.rajesh.jobportal.dto.JobResponseDto;
import com.rajesh.jobportal.repository.UserRepository;

import com.rajesh.jobportal.entity.Job;
import com.rajesh.jobportal.entity.User;
import com.rajesh.jobportal.service.JobService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobService jobService;
    private final UserRepository userRepository;

    public JobController(JobService jobService,
                         UserRepository userRepository) {
        this.jobService = jobService;
        this.userRepository = userRepository;
    }
    @PostMapping
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<JobResponseDto> createJob(
            @RequestBody @Valid Job job) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User recruiter = userRepository.findByEmail(email).orElse(null);

        if (recruiter == null) {
            return ResponseEntity.notFound().build();
        }

        job.setRecruiter(recruiter);

        Job savedJob = jobService.createJob(job);

        return ResponseEntity.ok(jobService.getJobById(savedJob.getId()));
    }

    @GetMapping
    public ResponseEntity<List<JobResponseDto>> getAllJobs() {
        return ResponseEntity.ok(jobService.getAllJobs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobResponseDto> getJobById(@PathVariable Long id) {

        JobResponseDto job = jobService.getJobById(id);

        if (job == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(job);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<JobResponseDto> updateJob(
            @PathVariable Long id,
            @RequestBody @Valid Job job) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User recruiter = userRepository.findByEmail(email).orElse(null);

        if (recruiter == null) {
            return ResponseEntity.notFound().build();
        }

        Job existingJob = jobService.getJobEntityById(id);

        if (existingJob == null) {
            return ResponseEntity.notFound().build();
        }

        if (!existingJob.getRecruiter().getId().equals(recruiter.getId())) {
            return ResponseEntity.status(403).build();
        }

        Job updatedJob = jobService.updateJob(id, job);

        return ResponseEntity.ok(jobService.getJobById(updatedJob.getId()));
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User recruiter = userRepository.findByEmail(email).orElse(null);

        if (recruiter == null) {
            return ResponseEntity.notFound().build();
        }

        Job existingJob = jobService.getJobEntityById(id);

        if (existingJob == null) {
            return ResponseEntity.notFound().build();
        }

        if (!existingJob.getRecruiter().getId().equals(recruiter.getId())) {
            return ResponseEntity.status(403).build();
        }

        jobService.deleteJob(id);

        return ResponseEntity.noContent().build();
    }
}