package org.example.medinsurance.mapper;

import org.example.medinsurance.dto.ClaimDTO;
import org.example.medinsurance.model.Claim;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ClaimMapper {

    ClaimMapper INSTANCE = Mappers.getMapper(ClaimMapper.class);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "policy.id", target = "policyId")
    ClaimDTO toDto(Claim claim);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "policyId", target = "policy.id")
    Claim toEntity(ClaimDTO dto);
}
