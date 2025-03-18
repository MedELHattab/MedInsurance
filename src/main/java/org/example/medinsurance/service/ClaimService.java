package org.example.medinsurance.service;

import org.example.medinsurance.dto.ClaimDTO;
import org.example.medinsurance.enums.ClaimStatus;

import java.util.List;

public interface ClaimService {

    ClaimDTO submitClaim(ClaimDTO claimDTO);

    List<ClaimDTO> getAllClaims();

    void updateClaimStatus(Long claimId, ClaimStatus status);

    List<ClaimDTO> getAuthUserClaims();
}
