package org.example.medinsurance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.example.medinsurance.enums.ClaimStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClaimDTO {
    private Long id;
    private Long userId;
    private Long policyId;
    private String userName;
    private String userEmail;
    private String policyName;
    private String policyCoverage;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "image is required")
    private String image;

    @NotNull(message = "amount is required")
    private Double amount;

    private LocalDateTime createdAt;
    private ClaimStatus status;
}