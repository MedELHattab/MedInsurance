package org.example.medinsurance.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RefundDTO {
    private Long id;
    private Long claimId;
    private Long userId;
    private String userName;
    private BigDecimal amount;
    private String reference;
    private LocalDateTime createdAt;
    private boolean isPaid;
}