package org.example.medinsurance.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.medinsurance.dto.SubscriptionDTO;
import org.example.medinsurance.model.Policy;
import org.example.medinsurance.model.PolicySubscription;
import org.example.medinsurance.model.User;
import org.example.medinsurance.enums.SubscriptionStatus;
import org.example.medinsurance.mapper.SubscriptionMapper;
import org.example.medinsurance.repository.PolicyRepository;
import org.example.medinsurance.repository.SubscriptionRepository;
import org.example.medinsurance.repository.UserRepository;
import org.example.medinsurance.service.SubscriptionService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final PolicyRepository policyRepository;
    private final SubscriptionMapper subscriptionMapper;

    @Override
    public SubscriptionDTO subscribeUser(Long policyId) {
        // Get the authenticated user's email
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        // Fetch the user from the database
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Fetch the policy
        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() -> new RuntimeException("Policy not found"));

        // Check if the user already has an active subscription
        if (subscriptionRepository.existsByUser(user)) {
            throw new RuntimeException("User already has an active subscription");
        }

        // Create and save the subscription
        PolicySubscription subscription = new PolicySubscription();
        subscription.setUser(user);
        subscription.setPolicy(policy);
        subscription.setStatus(SubscriptionStatus.ACTIVE);
        subscription.setSubscriptionDate(LocalDate.now());

        PolicySubscription savedSubscription = subscriptionRepository.save(subscription);
        return subscriptionMapper.toDto(savedSubscription);
    }

    @Override
    public SubscriptionDTO upgradeSubscription(Long newPolicyId) {
        // Get the authenticated user's email
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        // Fetch the user from the database
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Find the existing subscription for the authenticated user
        PolicySubscription existingSubscription = subscriptionRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("No active subscription found for this user"));

        // Fetch the new policy
        Policy newPolicy = policyRepository.findById(newPolicyId)
                .orElseThrow(() -> new RuntimeException("New policy not found"));

        // Update the subscription with the new policy and update date
        existingSubscription.setPolicy(newPolicy);
        existingSubscription.setSubscriptionDate(LocalDate.now());

        PolicySubscription updatedSubscription = subscriptionRepository.save(existingSubscription);
        return subscriptionMapper.toDto(updatedSubscription);
    }

    @Override
    public SubscriptionDTO getSubscriptionByUser(Long userId) {
        PolicySubscription subscription = subscriptionRepository.findByUser(
                userRepository.findById(Math.toIntExact(userId))
                        .orElseThrow(() -> new RuntimeException("User not found"))
        ).orElseThrow(() -> new RuntimeException("No active subscription found for this user"));

        return subscriptionMapper.toDto(subscription);
    }
}
