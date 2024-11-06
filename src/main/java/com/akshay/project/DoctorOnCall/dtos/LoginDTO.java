package com.akshay.project.DoctorOnCall.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String log_email;

    @NotBlank(message = "Password is required")
    private String log_password;
}