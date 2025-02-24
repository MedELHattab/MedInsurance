package org.example.medinsurance.service;

import org.example.medinsurance.dto.SubscriptionDTO;
import org.example.medinsurance.enums.SubscriptionStatus;

public interface SubscriptionService {
    SubscriptionDTO subscribeUser(Long userId);

    SubscriptionDTO upgradeSubscription(Long newPolicyId);

    SubscriptionDTO getSubscriptionByUser();

   SubscriptionDTO changeSubscriptionStatus(Long subscriptionId, SubscriptionStatus newStatus) ;

    }
