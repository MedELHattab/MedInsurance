package org.example.medinsurance.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.example.medinsurance.validation.MinAge;

import java.time.LocalDate;

@Getter
@Setter
public class RegisterUserDto {

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must be at most 100 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email must be at most 100 characters")
    private String email;

    @NotNull(message = "Password is required")
    private String password;

    @NotNull(message = "Birthday is required")
    @Past(message = "Birthday must be in the past")
    @MinAge(value = 18, message = "You must be at least 18 years old")
    private LocalDate birthday;

    @Size(max = 255, message = "Address must be at most 255 characters")
    private String address;

    @Pattern(regexp = "^\\+?[0-9\\s\\-\\(\\)]{8,20}$", message = "Phone number format is invalid")
    private String phone;

    private String image;
}