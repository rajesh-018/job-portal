package com.rajesh.jobportal.dto;

import com.rajesh.jobportal.enums.EmploymentType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobResponseDto {

    private Long id;
    private String title;
    private String description;
    private String companyName;
    private String location;
    private BigDecimal salary;
    private EmploymentType employmentType;
    private LocalDateTime createdAt;

    private Long recruiterId;
    private String recruiterName;
}