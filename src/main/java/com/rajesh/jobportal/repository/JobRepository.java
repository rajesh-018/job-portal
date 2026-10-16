package com.rajesh.jobportal.repository;

import com.rajesh.jobportal.entity.Job;
import com.rajesh.jobportal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {

    List<Job> findByRecruiter(User recruiter);

    List<Job> findByTitleContainingIgnoreCase(String title);
}