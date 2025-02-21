package org.example.medinsurance.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;

import lombok.NoArgsConstructor;
import org.example.medinsurance.enums.SubscriptionStatus;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "policy_subscriptions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PolicySubscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Ensure ID is auto-generated
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "policy_id", nullable = false)
    private Policy policy;

    @Enumerated(EnumType.STRING)
    private SubscriptionStatus status = SubscriptionStatus.ACTIVE;

    private LocalDate subscriptionDate;
}
