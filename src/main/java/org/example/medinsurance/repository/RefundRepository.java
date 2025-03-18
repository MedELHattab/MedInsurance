package org.example.medinsurance.repository;

import org.example.medinsurance.model.Claim;
import org.example.medinsurance.model.Refund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefundRepository extends JpaRepository<Refund, Long> {
    List<Refund> findByUserId(Long userId);
    Optional<Refund> findByClaimId(Long claimId);
    long countByIsPaid(boolean isPaid);
    Optional<Refund> findByClaim(Claim claim);
}