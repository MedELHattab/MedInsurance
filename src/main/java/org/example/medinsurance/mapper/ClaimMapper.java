package org.example.medinsurance.mapper;

import org.example.medinsurance.dto.ClaimDTO;
import org.example.medinsurance.model.Claim;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClaimMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userName", source = "user.username")
    @Mapping(target = "userEmail", source = "user.email")
    @Mapping(target = "policyId", source = "policy.id")
    @Mapping(target = "policyName", source = "policy.name")
    @Mapping(target = "policyCoverage", source="policy.percentage")
    ClaimDTO toDto(Claim claim);

    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "policy.id", source = "policyId")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    Claim toEntity(ClaimDTO claimDTO);
}