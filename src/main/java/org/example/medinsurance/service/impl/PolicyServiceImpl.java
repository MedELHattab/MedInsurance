package org.example.medinsurance.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.medinsurance.dto.PolicyDTO;
import org.example.medinsurance.enums.PolicyStatus;
import org.example.medinsurance.mapper.PolicyMapper;
import org.example.medinsurance.model.Policy;
import org.example.medinsurance.repository.PolicyRepository;
import org.example.medinsurance.service.PolicyService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PolicyServiceImpl implements PolicyService {

    private final PolicyRepository policyRepository;
    private final PolicyMapper policyMapper;

    @Override
    public PolicyDTO createPolicy(PolicyDTO policyDTO) {
        Policy policy = policyMapper.toEntity(policyDTO);
        return policyMapper.toDto(policyRepository.save(policy));
    }

    @Override
    public PolicyDTO updatePolicy(Long id, PolicyDTO policyDTO) {
        Policy policy = policyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Policy not found"));

        policy.setName(policyDTO.getName());
        policy.setDescription(policyDTO.getDescription());
        policy.setPercentage(policyDTO.getPercentage());
        policy.setStatus(policyDTO.getStatus());

        return policyMapper.toDto(policyRepository.save(policy));
    }

    @Override
    public void deletePolicy(Long id) {
        if (!policyRepository.existsById(id)) {
            throw new RuntimeException("Policy not found");
        }
        policyRepository.deleteById(id);
    }

    @Override
    public PolicyDTO getPolicyById(Long id) {
        Policy policy = policyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Policy not found"));
        return policyMapper.toDto(policy);
    }

    @Override
    public List<PolicyDTO> getAllPolicies() {
        return policyRepository.findAll().stream()
                .map(policyMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PolicyDTO> getActivePolicies() {
        return policyRepository.findByStatus(PolicyStatus.ACTIVE)
                .stream()
                .map(policyMapper::toDto)
                .collect(Collectors.toList());
    }
}
