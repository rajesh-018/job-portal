package com.rajesh.jobportal.repository;

import com.rajesh.jobportal.entity.Job;
import com.rajesh.jobportal.entity.JobApplication;
import com.rajesh.jobportal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobApplicationRepository
        extends JpaRepository<JobApplication, Long> {

    List<JobApplication> findByJobSeeker(User jobSeeker);

    List<JobApplication> findByJobRecruiter(User recruiter);
    boolean existsByJobAndJobSeeker(Job job, User jobSeeker);
}