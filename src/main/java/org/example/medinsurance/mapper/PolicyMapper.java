package org.example.medinsurance.mapper;

import org.example.medinsurance.dto.PolicyDTO;
import org.example.medinsurance.model.Policy;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PolicyMapper {
    PolicyMapper INSTANCE = Mappers.getMapper(PolicyMapper.class);

    PolicyDTO toDto(Policy policy);
    Policy toEntity(PolicyDTO policyDTO);
}
