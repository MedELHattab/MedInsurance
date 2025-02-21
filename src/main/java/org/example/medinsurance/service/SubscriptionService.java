package org.example.medinsurance.service;

import org.example.medinsurance.dto.SubscriptionDTO;
import org.example.medinsurance.model.PolicySubscription;

public interface SubscriptionService {
    SubscriptionDTO subscribeUser(Long userId, Long policyId);
    SubscriptionDTO upgradeSubscription(Long userId, Long newPolicyId);
    SubscriptionDTO getSubscriptionByUser(Long userId);
}
