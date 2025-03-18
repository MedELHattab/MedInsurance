package org.example.medinsurance.repository;

import org.example.medinsurance.enums.SubscriptionStatus;
import org.example.medinsurance.model.Policy;
import org.example.medinsurance.model.PolicySubscription;
import org.example.medinsurance.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<PolicySubscription, Long> {

    Optional<PolicySubscription> findByUser(User user); // A user should have only one subscription

    boolean existsByUser(User user); // Check if a user already has a subscription

    long countByStatus(SubscriptionStatus status);
    long countByPolicy(Policy policy);
}
