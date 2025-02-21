package org.example.medinsurance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClaimDTO {

    @NotNull(message = "Policy is required")
    private Long policyId;

    private String description;

    @NotBlank(message = "Image is required")
    private String image; // Base64 encoded image
}
