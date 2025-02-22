package org.example.medinsurance.service;

import org.example.medinsurance.dto.SubscriptionDTO;

public interface SubscriptionService {
    SubscriptionDTO subscribeUser(Long userId);

    SubscriptionDTO upgradeSubscription(Long newPolicyId);

    SubscriptionDTO getSubscriptionByUser(Long userId);
}
