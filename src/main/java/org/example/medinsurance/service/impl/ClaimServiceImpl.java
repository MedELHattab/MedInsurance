package org.example.medinsurance.service.impl;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.example.medinsurance.dto.ClaimDTO;
import org.example.medinsurance.dto.SubscriptionDTO;
import org.example.medinsurance.enums.ClaimStatus;
import org.example.medinsurance.enums.Role;
import org.example.medinsurance.enums.SubscriptionStatus;
import org.example.medinsurance.mapper.ClaimMapper;
import org.example.medinsurance.model.Claim;
import org.example.medinsurance.model.User;
import org.example.medinsurance.repository.ClaimRepository;
import org.example.medinsurance.repository.PolicyRepository;
import org.example.medinsurance.repository.UserRepository;
import org.example.medinsurance.service.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClaimServiceImpl implements ClaimService {

    private final ClaimRepository claimRepository;
    private final UserRepository userRepository;
    private final ClaimMapper claimMapper;
    private final UserService userService;
    private final FileStorageService fileStorageService;
    private final EmailService emailService;
    private final PolicyRepository policyRepository;
    private final SubscriptionService subscriptionService;
    private final RefundService refundService;

    @Override
    public ClaimDTO submitClaim(ClaimDTO claimDTO) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        // Fetch the user from the database
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Fetch the user's active subscription
        SubscriptionDTO subscriptionDTO = subscriptionService.getSubscriptionByUser();

        // Check if the subscription is canceled
        if (subscriptionDTO.getStatus() == SubscriptionStatus.CANCELED) {
            throw new RuntimeException("You cannot submit a claim with a canceled subscription");
        }

        // Create and map the claim
        Claim claim = claimMapper.toEntity(claimDTO);
        claim.setUser(user);
        claim.setPolicy(policyRepository.findById(subscriptionDTO.getPolicyId())
                .orElseThrow(() -> new RuntimeException("Policy not found")));
        claim.setStatus(ClaimStatus.PENDING);

        // Handle Base64 image storage
        if (claimDTO.getImage() != null && !claimDTO.getImage().isEmpty()) {
            String storedImageFilename = fileStorageService.storeBase64Image(claimDTO.getImage());
            claim.setImage(storedImageFilename);
        }

        Claim savedClaim = claimRepository.save(claim);
        sendClaimNotification(savedClaim);

        return claimMapper.toDto(savedClaim);
    }

    @Override
    public List<ClaimDTO> getAllClaims() {
        return claimRepository.findAll().stream()
                .map(claimMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void updateClaimStatus(Long claimId, ClaimStatus status) {
        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new RuntimeException("Claim not found"));

        ClaimStatus oldStatus = claim.getStatus();
        claim.setStatus(status);
        claimRepository.save(claim);

        // If the claim is being approved and wasn't already approved before
        if (status == ClaimStatus.APPROVED && oldStatus != ClaimStatus.APPROVED) {
            // Create a refund for the approved claim
            refundService.createRefund(claim);

            // Send approval notification
            try {
                String subject = "Your Claim Has Been Approved";
                String message = "Dear " + claim.getUser().getUsername() + ",\n\n" +
                        "We are pleased to inform you that your claim has been approved.\n" +
                        "A refund will be processed according to your policy coverage.\n\n" +
                        "Thank you for using our services.\n" +
                        "Insurance Admin Team";

                emailService.sendVerificationEmail(claim.getUser().getEmail(), subject, message);
            } catch (MessagingException e) {
                // Log error but don't stop the process
                System.err.println("Failed to send claim approval email: " + e.getMessage());
            }
        } else if (status == ClaimStatus.REJECTED) {
            // Send rejection notification
            try {
                String subject = "Your Claim Status Update";
                String message = "Dear " + claim.getUser().getUsername() + ",\n\n" +
                        "We regret to inform you that your claim has been rejected.\n" +
                        "If you have any questions, please contact our support team.\n\n" +
                        "Thank you for your understanding.\n" +
                        "Insurance Admin Team";

                emailService.sendVerificationEmail(claim.getUser().getEmail(), subject, message);
            } catch (MessagingException e) {
                // Log error but don't stop the process
                System.err.println("Failed to send claim rejection email: " + e.getMessage());
            }
        }
    }

    private void sendClaimNotification(Claim claim) {
        try {
            String clientEmail = claim.getUser().getEmail();
            String subject = "Claim Submitted Successfully";
            String message = "Dear " + claim.getUser().getUsername() + ", your claim has been submitted successfully.";

            // Notify the client
            emailService.sendVerificationEmail(clientEmail, subject, message);

            // Notify all employees
            List<User> employees = userRepository.findByRole(Role.valueOf("EMPLOYEE"));
            for (User employee : employees) {
                emailService.sendVerificationEmail(employee.getEmail(), "New Claim Submitted",
                        "A new claim has been submitted by user: " + claim.getUser().getUsername());
            }
        } catch (MessagingException e) {
            throw new RuntimeException("Error sending claim notification emails", e);
        }
    }

    @Override
    public List<ClaimDTO> getAuthUserClaims() {
        // Get the authenticated user
        User currentUser = userService.getAuthenticatedUser();

        // Get claims for this user
        List<Claim> userClaims = claimRepository.findByUserOrderByCreatedAtDesc(currentUser);

        // Use the mapper to convert claims to DTOs
        return userClaims.stream()
                .map(claimMapper::toDto)
                .collect(Collectors.toList());
    }
}