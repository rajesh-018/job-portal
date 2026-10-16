package com.rajesh.jobportal.dto;

import com.rajesh.jobportal.enums.ApplicationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobApplicationStatusRequestDto {

    @NotNull(message = "Application status is required")
    private ApplicationStatus status;
}