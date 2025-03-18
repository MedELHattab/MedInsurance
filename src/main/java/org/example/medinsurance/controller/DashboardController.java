package org.example.medinsurance.controller;

import lombok.RequiredArgsConstructor;
import org.example.medinsurance.dto.*;
import org.example.medinsurance.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/stats")
    public ResponseEntity<DashboardStatsDTO> getDashboardStats() {
        return ResponseEntity.ok(dashboardService.getDashboardStats());
    }

    @GetMapping("/policy-distribution")
    public ResponseEntity<List<PolicyDistributionDTO>> getPolicyDistribution() {
        return ResponseEntity.ok(dashboardService.getPolicyDistribution());
    }

    @GetMapping("/monthly-claims")
    public ResponseEntity<List<MonthlyClaimsDTO>> getMonthlyClaimsData() {
        return ResponseEntity.ok(dashboardService.getMonthlyClaimsData());
    }

    @GetMapping("/recent-claims")
    public ResponseEntity<List<RecentClaimDTO>> getRecentClaims() {
        return ResponseEntity.ok(dashboardService.getRecentClaims());
    }
}