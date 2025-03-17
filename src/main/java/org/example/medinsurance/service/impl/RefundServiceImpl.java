package org.example.medinsurance.service.impl;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.example.medinsurance.dto.RefundDTO;
import org.example.medinsurance.mapper.RefundMapper;
import org.example.medinsurance.model.Claim;
import org.example.medinsurance.model.Refund;
import org.example.medinsurance.repository.RefundRepository;
import org.example.medinsurance.service.EmailService;
import org.example.medinsurance.service.RefundService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RefundServiceImpl implements RefundService {

    private final RefundRepository refundRepository;
    private final RefundMapper refundMapper;
    private final EmailService emailService;

    @Override
    public RefundDTO createRefund(Claim claim) {
        // Check if a refund already exists for this claim
        if (refundRepository.findByClaimId(claim.getId()).isPresent()) {
            throw new RuntimeException("A refund already exists for this claim");
        }

        // Calculate refund amount based on policy coverage percentage
        BigDecimal  claimAmount = BigDecimal.valueOf(claim.getAmount()); // Default amount, replace with actual claim amount when added to model
        BigDecimal coveragePercentage = BigDecimal.valueOf(claim.getPolicy().getPercentage());
        BigDecimal refundAmount = claimAmount.multiply(coveragePercentage).divide(BigDecimal.valueOf(100));

        // Create refund
        Refund refund = Refund.builder()
                .claim(claim)
                .user(claim.getUser())
                .amount(refundAmount)
                .reference(generateReference())
                .createdAt(LocalDateTime.now())
                .isPaid(false)
                .build();

        Refund savedRefund = refundRepository.save(refund);

        // Send notification email about refund
        try {
            String subject = "Claim Approved and Refund Created";
            String message = "Dear " + claim.getUser().getUsername() + ",\n\n" +
                    "Your claim has been approved, and a refund has been created for you.\n" +
                    "Refund Reference: " + refund.getReference() + "\n" +
                    "Amount: $" + refund.getAmount() + "\n\n" +
                    "The refund will be processed shortly.\n\n" +
                    "Thank you for using our services.\n" +
                    "Insurance Admin Team";

            emailService.sendVerificationEmail(claim.getUser().getEmail(), subject, message);
        } catch (MessagingException e) {
            // Log error but don't stop the process
            System.err.println("Failed to send refund notification email: " + e.getMessage());
        }

        return refundMapper.toDto(savedRefund);
    }

    @Override
    public List<RefundDTO> getAllRefunds() {
        return refundRepository.findAll().stream()
                .map(refundMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RefundDTO> getRefundsByUser(Long userId) {
        return refundRepository.findByUserId(userId).stream()
                .map(refundMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public RefundDTO getRefundById(Long id) {
        return refundRepository.findById(id)
                .map(refundMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Refund not found"));
    }

    @Override
    public RefundDTO updatePaymentStatus(Long id, boolean paid) {
        Refund refund = refundRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Refund not found"));

        refund.setPaid(paid);
        refund = refundRepository.save(refund);

        // If marked as paid, send a notification
        if (paid) {
            try {
                String subject = "Refund Payment Processed";
                String message = "Dear " + refund.getUser().getUsername() + ",\n\n" +
                        "Your refund payment has been processed.\n" +
                        "Refund Reference: " + refund.getReference() + "\n" +
                        "Amount: $" + refund.getAmount() + "\n\n" +
                        "Thank you for using our services.\n" +
                        "Insurance Admin Team";

                emailService.sendVerificationEmail(refund.getUser().getEmail(), subject, message);
            } catch (MessagingException e) {
                // Log error but don't stop the process
                System.err.println("Failed to send payment notification email: " + e.getMessage());
            }
        }

        return refundMapper.toDto(refund);
    }

    /**
     * Generate a unique reference number for the refund
     *
     * @return A unique reference string
     */
    private String generateReference() {
        return "RF-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}