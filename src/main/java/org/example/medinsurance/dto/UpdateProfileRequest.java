package org.example.medinsurance.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateProfileRequest {

    @Size(min = 3, max = 100)
    private String name;

    @Email(message = "Email should be valid")
    private String email;

    private String password;
    
    private String image;
}
