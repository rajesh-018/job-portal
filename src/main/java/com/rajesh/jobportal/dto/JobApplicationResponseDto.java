package com.rajesh.jobportal.dto;

import com.rajesh.jobportal.enums.ApplicationStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobApplicationResponseDto {

    private Long id;

    private Long jobId;
    private String jobTitle;

    private Long jobSeekerId;
    private String jobSeekerName;

    private ApplicationStatus status;
    private LocalDateTime appliedAt;
}