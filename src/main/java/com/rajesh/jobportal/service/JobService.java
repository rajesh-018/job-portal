package com.rajesh.jobportal.service;

import com.rajesh.jobportal.dto.JobResponseDto;
import com.rajesh.jobportal.entity.Job;

import java.util.List;

public interface JobService {

    Job createJob(Job job);

    List<JobResponseDto> getAllJobs();

    JobResponseDto getJobById(Long id);

    Job updateJob(Long id, Job job);

    void deleteJob(Long id);

    Job getJobEntityById(Long id);
}