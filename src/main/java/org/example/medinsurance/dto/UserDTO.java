
package org.example.medinsurance.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import org.example.medinsurance.model.User;
import org.example.medinsurance.enums.Role;

@Data
@Builder
public class UserDTO {

    private Long id;

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must be at most 100 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email must be at most 100 characters")
    private String email;

    @NotNull(message = "Password is required")
    private String password;

    @NotNull(message = "Age is required")
    @Min(value = 18, message = "Age must be at least 18")
    private Integer age;

    @NotNull(message = "Monthly income is required")
    @PositiveOrZero(message = "Monthly income must be zero or positive")
    private Double monthlyIncome;

    @NotNull(message = "Credit score is required")
    @Min(value = 300, message = "Credit score must be at least 300")
    @Max(value = 850, message = "Credit score must be at most 850")
    private Integer creditScore;

    @NotNull(message = "Role is required")
    private Role role;

}
