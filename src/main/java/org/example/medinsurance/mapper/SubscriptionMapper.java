package org.example.medinsurance.mapper;

import org.example.medinsurance.dto.SubscriptionDTO;
import org.example.medinsurance.model.PolicySubscription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {

    SubscriptionMapper INSTANCE = Mappers.getMapper(SubscriptionMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "policy.id", target = "policyId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "subscriptionDate", target = "subscriptionDate")
    SubscriptionDTO toDto(PolicySubscription entity);

    @Mapping(source = "policyId", target = "policy.id")
    @Mapping(source = "userId", target = "user.id")
    PolicySubscription toEntity(SubscriptionDTO dto);
}
