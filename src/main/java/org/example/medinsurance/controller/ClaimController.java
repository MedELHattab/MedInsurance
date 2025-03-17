package org.example.medinsurance.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.medinsurance.dto.ClaimDTO;
import org.example.medinsurance.enums.ClaimStatus;
import org.example.medinsurance.service.ClaimService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/api/claims")
@RequiredArgsConstructor
public class ClaimController {

    private final ClaimService claimService;

    @PostMapping(value = "/submit", consumes = {"multipart/form-data"})
    public ResponseEntity<ClaimDTO> submitClaim(
            @RequestParam("description") String description,
            @RequestParam("amount") String amount,
            @RequestParam(value = "image", required = false) MultipartFile image) {

        ClaimDTO claimDTO = new ClaimDTO();
        claimDTO.setDescription(description);
        claimDTO.setAmount(Double.valueOf(amount));

        // Convert the image to Base64 if provided
        if (image != null && !image.isEmpty()) {
            try {
                String base64Image = Base64.getEncoder().encodeToString(image.getBytes());
                claimDTO.setImage(base64Image);
            } catch (IOException e) {
                return ResponseEntity.badRequest().body(null);
            }
        }

        return ResponseEntity.ok(claimService.submitClaim(claimDTO));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<List<ClaimDTO>> getAllClaims() {
        return ResponseEntity.ok(claimService.getAllClaims());
    }

    @PutMapping("/{claimId}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<String> updateClaimStatus(@PathVariable Long claimId,
                                                    @RequestParam ClaimStatus status) {
        claimService.updateClaimStatus(claimId, status);
        return ResponseEntity.ok("Claim status updated successfully!");
    }
}
