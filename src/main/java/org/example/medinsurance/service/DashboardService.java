package org.example.medinsurance.service;

import org.example.medinsurance.dto.DashboardStatsDTO;
import org.example.medinsurance.dto.MonthlyClaimsDTO;
import org.example.medinsurance.dto.PolicyDistributionDTO;
import org.example.medinsurance.dto.RecentClaimDTO;

import java.util.List;

public interface DashboardService {
    /**
     * Get dashboard statistics
     * @return DashboardStatsDTO with all dashboard statistics
     */
    DashboardStatsDTO getDashboardStats();

    /**
     * Get policy distribution data for charts
     * @return List of PolicyDistributionDTO with policy name, count and percentage
     */
    List<PolicyDistributionDTO> getPolicyDistribution();

    /**
     * Get monthly claims data for charts
     * @return List of MonthlyClaimsDTO with month and claims count by status
     */
    List<MonthlyClaimsDTO> getMonthlyClaimsData();

    /**
     * Get recent claims for dashboard display
     * @return List of RecentClaimDTO with claim details
     */
    List<RecentClaimDTO> getRecentClaims();
}