package org.example.medinsurance.dto;

import jakarta.validation.constraints.*;

import lombok.*;
import org.example.medinsurance.enums.PolicyStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PolicyDTO {

    private String id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Description is required")
    private String description;

    @Min(value = 0, message = "Percentage must be at least 0")
    @Max(value = 100, message = "Percentage cannot exceed 100")
    private double percentage;

    @NotNull(message = "Status is required")
    private PolicyStatus status;
}
