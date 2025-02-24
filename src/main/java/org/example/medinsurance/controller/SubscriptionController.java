package org.example.medinsurance.controller;

import lombok.RequiredArgsConstructor;
import org.example.medinsurance.dto.SubscriptionDTO;
import org.example.medinsurance.enums.SubscriptionStatus;
import org.example.medinsurance.service.SubscriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping("/subscribe")
    public ResponseEntity<SubscriptionDTO> subscribeUser(@RequestParam Long policyId) {
        return ResponseEntity.ok(subscriptionService.subscribeUser(policyId));
    }


    @PutMapping("/upgrade")
    public ResponseEntity<SubscriptionDTO> upgradeSubscription(
            @RequestParam Long newPolicyId) {
        return ResponseEntity.ok(subscriptionService.upgradeSubscription(newPolicyId));
    }

    @GetMapping("/my-subscription")
    public ResponseEntity<SubscriptionDTO> getSubscriptionByUser() {
        return ResponseEntity.ok(subscriptionService.getSubscriptionByUser());
    }

    @PutMapping("/{subscriptionId}/status")
    @PreAuthorize("hasRole('EMPLOYEE')") // Only EMPLOYEE can access this endpoint
    public ResponseEntity<String> updateSubscriptionStatus(
            @PathVariable Long subscriptionId,
            @RequestParam SubscriptionStatus newStatus) {

        subscriptionService.changeSubscriptionStatus(subscriptionId, newStatus);
        return ResponseEntity.ok("Subscription status updated successfully!");
    }

}
