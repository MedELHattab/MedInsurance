package org.example.medinsurance.controller;

import lombok.RequiredArgsConstructor;
import org.example.medinsurance.dto.SubscriptionDTO;
import org.example.medinsurance.service.SubscriptionService;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/user/{userId}")
    public ResponseEntity<SubscriptionDTO> getSubscriptionByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(subscriptionService.getSubscriptionByUser(userId));
    }
}
