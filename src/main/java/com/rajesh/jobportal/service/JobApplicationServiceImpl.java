package com.rajesh.jobportal.service;

import com.rajesh.jobportal.dto.JobApplicationResponseDto;
import com.rajesh.jobportal.dto.JobApplicationStatusRequestDto;
import com.rajesh.jobportal.entity.JobApplication;
import com.rajesh.jobportal.entity.User;
import com.rajesh.jobportal.exception.DuplicateApplicationException;
import com.rajesh.jobportal.exception.ResourceNotFoundException;
import com.rajesh.jobportal.exception.UnauthorizedActionException;
import com.rajesh.jobportal.repository.JobApplicationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobApplicationServiceImpl implements JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;

    public JobApplicationServiceImpl(
            JobApplicationRepository jobApplicationRepository) {
        this.jobApplicationRepository = jobApplicationRepository;
    }

    @Override
    public JobApplication applyForJob(JobApplication application) {

        boolean alreadyApplied =
                jobApplicationRepository.existsByJobAndJobSeeker(
                        application.getJob(),
                        application.getJobSeeker()
                );

        if (alreadyApplied) {
            throw new DuplicateApplicationException(
                    "You have already applied for this job"
            );
        }

        return jobApplicationRepository.save(application);
    }

    @Override
    public List<JobApplicationResponseDto> getMyApplications(
            Long jobSeekerId) {

        User user = new User();
        user.setId(jobSeekerId);

        return jobApplicationRepository.findByJobSeeker(user)
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    @Override
    public List<JobApplicationResponseDto> getApplicationsForRecruiter(
            Long recruiterId) {

        User user = new User();
        user.setId(recruiterId);

        return jobApplicationRepository.findByJobRecruiter(user)
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    @Override
    public JobApplicationResponseDto updateApplicationStatus(
            Long applicationId,
            JobApplicationStatusRequestDto request,
            Long recruiterId) {

        JobApplication application =
                jobApplicationRepository.findById(applicationId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Job application not found with id: "
                                                + applicationId
                                ));

        Long applicationRecruiterId =
                application.getJob().getRecruiter().getId();

        if (!applicationRecruiterId.equals(recruiterId)) {
            throw new UnauthorizedActionException(
                    "You are not authorized to update this application"
            );
        }

        application.setStatus(request.getStatus());

        JobApplication updatedApplication =
                jobApplicationRepository.save(application);

        return convertToDto(updatedApplication);
    }

    private JobApplicationResponseDto convertToDto(
            JobApplication application) {

        JobApplicationResponseDto dto =
                new JobApplicationResponseDto();

        dto.setId(application.getId());
        dto.setJobId(application.getJob().getId());
        dto.setJobTitle(application.getJob().getTitle());
        dto.setJobSeekerId(application.getJobSeeker().getId());
        dto.setJobSeekerName(application.getJobSeeker().getName());
        dto.setStatus(application.getStatus());
        dto.setAppliedAt(application.getAppliedAt());

        return dto;
    }
}