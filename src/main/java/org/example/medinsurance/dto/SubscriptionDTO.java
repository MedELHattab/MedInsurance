package org.example.medinsurance.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.medinsurance.enums.SubscriptionStatus;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionDTO {
    private Long id;
    private Long policyId;
    private Long userId;
    private SubscriptionStatus status;
    private LocalDate subscriptionDate;
}
