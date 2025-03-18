package org.example.medinsurance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyClaimsDTO {
    private String month;
    private long pendingClaims;
    private long approvedClaims;
    private long rejectedClaims;
}