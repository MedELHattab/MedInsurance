package org.example.medinsurance.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PolicyDistributionDTO {
    private String policyName;
    private long subscriptionCount;
    private double percentage;
}
