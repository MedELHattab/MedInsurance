package org.example.medinsurance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecentClaimDTO {
    private Long id;
    private String userName;
    private String userImage;
    private String policyName;
    private String policyId;
    private String description;
    private Double amount;
    private LocalDateTime date;
    private String status;
}
