package org.example.medinsurance.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateProfileRequest {
    private String name;

    @Email(message = "Email should be valid")
    private String email;

    private String image; // Optional profile image URL
}
