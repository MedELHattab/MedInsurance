package org.example.medinsurance.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.example.medinsurance.validation.MinAge;

import java.time.LocalDate;

@Data
public class UpdateProfileRequest {

    @Size(min = 3, max = 100)
    private String name;

    @Email(message = "Email should be valid")
    private String email;

    private String password;

    @Past(message = "Birthday must be in the past")
    @MinAge(value = 18, message = "You must be at least 18 years old")
    private LocalDate birthday;

    @Size(max = 255, message = "Address must be at most 255 characters")
    private String address;

    @Pattern(regexp = "^\\+?[0-9\\s\\-\\(\\)]{8,20}$", message = "Phone number format is invalid")
    private String phone;

    private String image;
}