package com.rajesh.jobportal.service;

import com.rajesh.jobportal.dto.JobApplicationResponseDto;
import com.rajesh.jobportal.dto.JobApplicationStatusRequestDto;
import com.rajesh.jobportal.entity.JobApplication;

import java.util.List;

public interface JobApplicationService {

    JobApplication applyForJob(JobApplication application);

    List<JobApplicationResponseDto> getMyApplications(Long jobSeekerId);

    List<JobApplicationResponseDto> getApplicationsForRecruiter(Long recruiterId);

    JobApplicationResponseDto updateApplicationStatus(
            Long applicationId,
            JobApplicationStatusRequestDto request,
            Long recruiterId
    );
}