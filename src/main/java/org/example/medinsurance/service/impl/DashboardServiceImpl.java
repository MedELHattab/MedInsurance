package org.example.medinsurance.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.medinsurance.dto.*;
import org.example.medinsurance.enums.ClaimStatus;
import org.example.medinsurance.enums.SubscriptionStatus;
import org.example.medinsurance.model.Claim;
import org.example.medinsurance.model.Policy;
import org.example.medinsurance.model.Refund;
import org.example.medinsurance.repository.ClaimRepository;
import org.example.medinsurance.repository.PolicyRepository;
import org.example.medinsurance.repository.SubscriptionRepository;
import org.example.medinsurance.repository.RefundRepository;
import org.example.medinsurance.repository.UserRepository;
import org.example.medinsurance.service.DashboardService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final UserRepository userRepository;
    private final PolicyRepository policyRepository;
    private final SubscriptionRepository policySubscriptionRepository;
    private final ClaimRepository claimRepository;
    private final RefundRepository refundRepository;

    @Override
    public DashboardStatsDTO getDashboardStats() {
        // Get counts from repositories
        long totalUsers = userRepository.count();
        long totalPolicies = policyRepository.count();
        long totalSubscriptions = policySubscriptionRepository.count();
        long activeSubscriptions = policySubscriptionRepository.countByStatus(SubscriptionStatus.ACTIVE);

        long totalClaims = claimRepository.count();
        long pendingClaims = claimRepository.countByStatus(ClaimStatus.PENDING);
        long approvedClaims = claimRepository.countByStatus(ClaimStatus.APPROVED);
        long rejectedClaims = claimRepository.countByStatus(ClaimStatus.REJECTED);

        BigDecimal totalRefundAmount = refundRepository.findAll().stream()
                .map(Refund::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long paidRefunds = refundRepository.countByIsPaid(true);
        long pendingRefunds = refundRepository.countByIsPaid(false);

        // Build and return the DTO
        return DashboardStatsDTO.builder()
                .totalUsers(totalUsers)
                .totalPolicies(totalPolicies)
                .totalSubscriptions(totalSubscriptions)
                .activeSubscriptions(activeSubscriptions)
                .totalClaims(totalClaims)
                .pendingClaims(pendingClaims)
                .approvedClaims(approvedClaims)
                .rejectedClaims(rejectedClaims)
                .totalRefundAmount(totalRefundAmount)
                .paidRefunds(paidRefunds)
                .pendingRefunds(pendingRefunds)
                .build();
    }

    @Override
    public List<PolicyDistributionDTO> getPolicyDistribution() {
        List<Policy> policies = policyRepository.findAll();
        Map<Policy, Long> subscriptionCounts = new HashMap<>();

        // Count subscriptions per policy
        for (Policy policy : policies) {
            long count = policySubscriptionRepository.countByPolicy(policy);
            subscriptionCounts.put(policy, count);
        }

        // Calculate total subscriptions
        long totalSubscriptions = subscriptionCounts.values().stream()
                .mapToLong(Long::longValue)
                .sum();

        // Build DTOs with percentage
        return subscriptionCounts.entrySet().stream()
                .map(entry -> {
                    Policy policy = entry.getKey();
                    long count = entry.getValue();
                    double percentage = totalSubscriptions > 0
                            ? (count * 100.0) / totalSubscriptions
                            : 0.0;

                    return PolicyDistributionDTO.builder()
                            .policyName(policy.getName())
                            .subscriptionCount(count)
                            .percentage(Math.round(percentage * 10) / 10.0) // Round to 1 decimal place
                            .build();
                })
                .sorted((a, b) -> Long.compare(b.getSubscriptionCount(), a.getSubscriptionCount())) // Sort by count descending
                .collect(Collectors.toList());
    }

    @Override
    public List<MonthlyClaimsDTO> getMonthlyClaimsData() {
        // Get current year and month
        int currentYear = LocalDateTime.now().getYear();
        int currentMonth = LocalDateTime.now().getMonthValue();

        // Initialize result list with past 6 months
        List<MonthlyClaimsDTO> result = new ArrayList<>();

        // Loop through the last 6 months
        for (int i = 5; i >= 0; i--) {
            int month = currentMonth - i;
            int year = currentYear;

            // Adjust for previous year if needed
            if (month <= 0) {
                month += 12;
                year -= 1;
            }

            // Get month name
            String monthName = Month.of(month).getDisplayName(TextStyle.SHORT, Locale.ENGLISH);

            // Get claims counts for this month
            LocalDateTime startOfMonth = LocalDateTime.of(year, month, 1, 0, 0);
            LocalDateTime endOfMonth = startOfMonth.plusMonths(1).minusSeconds(1);

            long pendingCount = claimRepository.countByStatusAndCreatedAtBetween(ClaimStatus.PENDING, startOfMonth, endOfMonth);
            long approvedCount = claimRepository.countByStatusAndCreatedAtBetween(ClaimStatus.APPROVED, startOfMonth, endOfMonth);
            long rejectedCount = claimRepository.countByStatusAndCreatedAtBetween(ClaimStatus.REJECTED, startOfMonth, endOfMonth);

            // Add to result
            result.add(MonthlyClaimsDTO.builder()
                    .month(monthName)
                    .pendingClaims(pendingCount)
                    .approvedClaims(approvedCount)
                    .rejectedClaims(rejectedCount)
                    .build());
        }

        return result;
    }

    @Override
    public List<RecentClaimDTO> getRecentClaims() {
        // Get 10 most recent claims
        List<Claim> recentClaims = claimRepository.findTop10ByOrderByCreatedAtDesc();

        // Map to DTOs
        return recentClaims.stream()
                .map(claim -> {
                    // Find the related refund if it exists
                    Refund refund = refundRepository.findByClaim(claim).orElse(null);

                    // Convert BigDecimal to Double
                    Double amount = refund != null ? refund.getAmount().doubleValue() : 0.0;

                    return RecentClaimDTO.builder()
                            .id(claim.getId())
                            .userName(claim.getUser().getName())
                            .userImage(claim.getUser().getImage())
                            .policyName(claim.getPolicy().getName())
                            .policyId(claim.getPolicy().getId().toString())
                            .description(claim.getDescription())
                            .amount(amount) // Now passing a Double
                            .date(claim.getCreatedAt())
                            .status(claim.getStatus().name())
                            .build();
                })
                .collect(Collectors.toList());
    }
}