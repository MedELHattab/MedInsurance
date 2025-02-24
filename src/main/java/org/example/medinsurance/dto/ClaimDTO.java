package org.example.medinsurance.dto;

import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "image is required")
    private String image;

    private LocalDateTime createdAt;
    private ClaimStatus status;
}
