package org.example.medinsurance.service;

import org.example.medinsurance.dto.PolicyDTO;
import java.util.List;

public interface PolicyService {
    PolicyDTO createPolicy(PolicyDTO policyDTO);
    PolicyDTO updatePolicy(Long id, PolicyDTO policyDTO);
    void deletePolicy(Long id);
    PolicyDTO getPolicyById(Long id);
    List<PolicyDTO> getAllPolicies();
    List<PolicyDTO> getActivePolicies();
}
