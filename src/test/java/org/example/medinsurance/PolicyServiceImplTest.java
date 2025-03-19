package org.example.medinsurance;

import org.example.medinsurance.dto.PolicyDTO;
import org.example.medinsurance.enums.PolicyStatus;
import org.example.medinsurance.mapper.PolicyMapper;
import org.example.medinsurance.model.Policy;
import org.example.medinsurance.repository.PolicyRepository;
import org.example.medinsurance.service.impl.PolicyServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PolicyServiceImplTest {

    @Mock
    private PolicyRepository policyRepository;

    @Mock
    private PolicyMapper policyMapper;

    @InjectMocks
    private PolicyServiceImpl policyService;

    private PolicyDTO policyDTO;
    private Policy policy;

    @BeforeEach
    void setUp() {
        // Initialize test data
        policyDTO = new PolicyDTO();
        policyDTO.setId("1"); // Changed from Long to String
        policyDTO.setName("Health Insurance");
        policyDTO.setDescription("Comprehensive health coverage");
        policyDTO.setPercentage(80.0);
        policyDTO.setStatus(PolicyStatus.ACTIVE);

        policy = new Policy();
        policy.setId(1L);
        policy.setName("Health Insurance");
        policy.setDescription("Comprehensive health coverage");
        policy.setPercentage(80.0);
        policy.setStatus(PolicyStatus.ACTIVE);
    }

    @Test
    void createPolicy_ShouldReturnCreatedPolicyDTO() {
        // Arrange
        when(policyMapper.toEntity(any(PolicyDTO.class))).thenReturn(policy);
        when(policyRepository.save(any(Policy.class))).thenReturn(policy);
        when(policyMapper.toDto(any(Policy.class))).thenReturn(policyDTO);

        // Act
        PolicyDTO result = policyService.createPolicy(policyDTO);

        // Assert
        assertNotNull(result);
        assertEquals(policyDTO.getId(), result.getId());
        assertEquals(policyDTO.getName(), result.getName());
        verify(policyMapper, times(1)).toEntity(policyDTO);
        verify(policyRepository, times(1)).save(policy);
        verify(policyMapper, times(1)).toDto(policy);
    }

    @Test
    void updatePolicy_WithExistingId_ShouldReturnUpdatedPolicyDTO() {
        // Arrange
        Long id = 1L;
        PolicyDTO updatedDTO = new PolicyDTO();
        updatedDTO.setId("1"); // Changed from Long to String
        updatedDTO.setName("Updated Health Insurance");
        updatedDTO.setDescription("Updated description");
        updatedDTO.setPercentage(90.0);
        updatedDTO.setStatus(PolicyStatus.DISABLED); // Assuming status is INACTIVE instead of DISABLED

        when(policyRepository.findById(id)).thenReturn(Optional.of(policy));
        when(policyRepository.save(any(Policy.class))).thenReturn(policy);
        when(policyMapper.toDto(any(Policy.class))).thenReturn(updatedDTO);

        // Act
        PolicyDTO result = policyService.updatePolicy(id, updatedDTO);

        // Assert
        assertNotNull(result);
        assertEquals(updatedDTO.getName(), result.getName());
        assertEquals(updatedDTO.getDescription(), result.getDescription());
        assertEquals(updatedDTO.getPercentage(), result.getPercentage());
        assertEquals(updatedDTO.getStatus(), result.getStatus());

        verify(policyRepository, times(1)).findById(id);
        verify(policyRepository, times(1)).save(policy);
        verify(policyMapper, times(1)).toDto(policy);
    }

    @Test
    void updatePolicy_WithNonExistingId_ShouldThrowException() {
        // Arrange
        Long id = 999L;
        when(policyRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            policyService.updatePolicy(id, policyDTO);
        });

        assertEquals("Policy not found", exception.getMessage());
        verify(policyRepository, times(1)).findById(id);
        verify(policyRepository, never()).save(any(Policy.class));
    }

    @Test
    void deletePolicy_WithExistingId_ShouldDeletePolicy() {
        // Arrange
        Long id = 1L;
        when(policyRepository.existsById(id)).thenReturn(true);
        doNothing().when(policyRepository).deleteById(id);

        // Act
        policyService.deletePolicy(id);

        // Assert
        verify(policyRepository, times(1)).existsById(id);
        verify(policyRepository, times(1)).deleteById(id);
    }

    @Test
    void deletePolicy_WithNonExistingId_ShouldThrowException() {
        // Arrange
        Long id = 999L;
        when(policyRepository.existsById(id)).thenReturn(false);

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            policyService.deletePolicy(id);
        });

        assertEquals("Policy not found", exception.getMessage());
        verify(policyRepository, times(1)).existsById(id);
        verify(policyRepository, never()).deleteById(anyLong());
    }

    @Test
    void getPolicyById_WithExistingId_ShouldReturnPolicyDTO() {
        // Arrange
        Long id = 1L;
        when(policyRepository.findById(id)).thenReturn(Optional.of(policy));
        when(policyMapper.toDto(policy)).thenReturn(policyDTO);

        // Act
        PolicyDTO result = policyService.getPolicyById(id);

        // Assert
        assertNotNull(result);
        assertEquals(policyDTO.getId(), result.getId());
        assertEquals(policyDTO.getName(), result.getName());
        verify(policyRepository, times(1)).findById(id);
        verify(policyMapper, times(1)).toDto(policy);
    }

    @Test
    void getPolicyById_WithNonExistingId_ShouldThrowException() {
        // Arrange
        Long id = 999L;
        when(policyRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            policyService.getPolicyById(id);
        });

        assertEquals("Policy not found", exception.getMessage());
        verify(policyRepository, times(1)).findById(id);
        verify(policyMapper, never()).toDto(any(Policy.class));
    }

    @Test
    void getAllPolicies_ShouldReturnAllPolicies() {
        // Arrange
        List<Policy> policies = Arrays.asList(
                policy,
                createPolicy(2L, "Dental Insurance", "Dental coverage", 70.0, PolicyStatus.ACTIVE)
        );
        List<PolicyDTO> policyDTOs = Arrays.asList(
                policyDTO,
                createPolicyDTO("2", "Dental Insurance", "Dental coverage", 70.0, PolicyStatus.ACTIVE) // Changed first param from Long to String
        );

        when(policyRepository.findAll()).thenReturn(policies);
        when(policyMapper.toDto(policies.get(0))).thenReturn(policyDTOs.get(0));
        when(policyMapper.toDto(policies.get(1))).thenReturn(policyDTOs.get(1));

        // Act
        List<PolicyDTO> result = policyService.getAllPolicies();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(policyDTOs.get(0).getName(), result.get(0).getName());
        assertEquals(policyDTOs.get(1).getName(), result.get(1).getName());
        verify(policyRepository, times(1)).findAll();
        verify(policyMapper, times(2)).toDto(any(Policy.class));
    }

    @Test
    void getActivePolicies_ShouldReturnOnlyActivePolicies() {
        // Arrange
        List<Policy> activePolicies = Arrays.asList(
                policy,
                createPolicy(2L, "Dental Insurance", "Dental coverage", 70.0, PolicyStatus.ACTIVE)
        );
        List<PolicyDTO> activePolicyDTOs = Arrays.asList(
                policyDTO,
                createPolicyDTO("2", "Dental Insurance", "Dental coverage", 70.0, PolicyStatus.ACTIVE) // Changed first param from Long to String
        );

        when(policyRepository.findByStatus(PolicyStatus.ACTIVE)).thenReturn(activePolicies);
        when(policyMapper.toDto(activePolicies.get(0))).thenReturn(activePolicyDTOs.get(0));
        when(policyMapper.toDto(activePolicies.get(1))).thenReturn(activePolicyDTOs.get(1));

        // Act
        List<PolicyDTO> result = policyService.getActivePolicies();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(activePolicyDTOs.get(0).getName(), result.get(0).getName());
        assertEquals(activePolicyDTOs.get(1).getName(), result.get(1).getName());
        assertEquals(PolicyStatus.ACTIVE, result.get(0).getStatus());
        assertEquals(PolicyStatus.ACTIVE, result.get(1).getStatus());
        verify(policyRepository, times(1)).findByStatus(PolicyStatus.ACTIVE);
        verify(policyMapper, times(2)).toDto(any(Policy.class));
    }

    // Helper method to create Policy objects
    private Policy createPolicy(Long id, String name, String description, Double percentage, PolicyStatus status) {
        Policy policy = new Policy();
        policy.setId(id);
        policy.setName(name);
        policy.setDescription(description);
        policy.setPercentage(percentage);
        policy.setStatus(status);
        return policy;
    }

    // Helper method to create PolicyDTO objects - Changed parameter type from Long to String
    private PolicyDTO createPolicyDTO(String id, String name, String description, Double percentage, PolicyStatus status) {
        PolicyDTO policyDTO = new PolicyDTO();
        policyDTO.setId(id);
        policyDTO.setName(name);
        policyDTO.setDescription(description);
        policyDTO.setPercentage(percentage);
        policyDTO.setStatus(status);
        return policyDTO;
    }
}