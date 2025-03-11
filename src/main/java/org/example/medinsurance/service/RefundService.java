package org.example.medinsurance.service;

import org.example.medinsurance.dto.RefundDTO;
import org.example.medinsurance.model.Claim;

import java.util.List;

public interface RefundService {

    /**
     * Create a new refund for an approved claim
     *
     * @param claim The approved claim
     * @return The created refund
     */
    RefundDTO createRefund(Claim claim);

    /**
     * Get all refunds
     *
     * @return List of refunds
     */
    List<RefundDTO> getAllRefunds();

    /**
     * Get refunds for a specific user
     *
     * @param userId User ID
     * @return List of refunds for the user
     */
    List<RefundDTO> getRefundsByUser(Long userId);

    /**
     * Get a refund by ID
     *
     * @param id Refund ID
     * @return Refund DTO
     */
    RefundDTO getRefundById(Long id);

    /**
     * Update refund payment status
     *
     * @param id Refund ID
     * @param paid New paid status
     * @return Updated refund
     */
    RefundDTO updatePaymentStatus(Long id, boolean paid);
}