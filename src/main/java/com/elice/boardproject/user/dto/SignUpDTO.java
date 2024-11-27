package com.elice.boardproject.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class SignUpDTO {

    @NotBlank(message = "User ID is required")
    @Size(min = 4, max = 10, message = "ID must be between 4 and 10 characters")
    private String userId;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 13, message = "Password must be between 8 and 13 characters")
    private String userPassword;

    @NotBlank(message = "User Name is required")
    private String userName;

    @NotBlank(message = "Contact is required")
    private String contact;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Email should match the required pattern")
    private String email;
}