package org.example.medinsurance.repository;

import org.example.medinsurance.enums.ClaimStatus;
import org.example.medinsurance.model.Claim;
import org.example.medinsurance.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ClaimRepository extends JpaRepository<Claim, Long> {
    List<Claim> findByStatus(String status);
    List<Claim> findTop10ByOrderByCreatedAtDesc();
    long countByStatus(ClaimStatus status);
    List<Claim> findByUserOrderByCreatedAtDesc(User user);
    long countByStatusAndCreatedAtBetween(ClaimStatus status, LocalDateTime start, LocalDateTime end);
}
