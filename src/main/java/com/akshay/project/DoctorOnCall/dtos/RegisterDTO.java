package com.akshay.project.DoctorOnCall.dtos;

import com.akshay.project.DoctorOnCall.enums.ROLE;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDTO {

    @NotBlank(message = "Name is required")
    private String reg_name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String reg_email;

    @NotBlank(message = "Password is required")
    private String reg_password;

    @NotNull(message = "Role is required")
    private ROLE reg_role;

}
