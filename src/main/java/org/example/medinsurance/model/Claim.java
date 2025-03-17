package org.example.medinsurance.model;

import jakarta.persistence.*;
import lombok.*;
import org.example.medinsurance.enums.ClaimStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "claims")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Claim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // Updated from Client to User

    @ManyToOne
    @JoinColumn(name = "policy_id", nullable = false)
    private Policy policy;

    private String description;
    private String image;

    private Double amount;
    private LocalDateTime createdAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private ClaimStatus status = ClaimStatus.PENDING;
}

