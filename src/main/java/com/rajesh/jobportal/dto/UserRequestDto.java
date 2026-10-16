package com.rajesh.jobportal.dto;

import com.rajesh.jobportal.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDto {

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    private String email;

    @NotNull(message = "Phone number is required")
    private Long phoneNumber;

    @NotBlank(message = "Password cannot be blank")
    @Size(
            min = 6,
            message = "Password must contain at least 6 characters"
    )
    private String password;

    @NotNull(message = "Role is required")
    private Role role;
}