package org.example.medinsurance.mapper;

import org.example.medinsurance.dto.RefundDTO;
import org.example.medinsurance.model.Refund;
import org.example.medinsurance.model.Claim;
import org.example.medinsurance.model.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface RefundMapper {

    @Mapping(target = "claimId", source = "claim.id")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userName", source = "user.username")
    RefundDTO toDto(Refund refund);

    @Mapping(target = "claim", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Refund toEntity(RefundDTO refundDTO);

    @AfterMapping
    default void mapClaimAndUser(@MappingTarget Refund refund, RefundDTO refundDTO) {
        // These relationships would typically be set by the service layer
        // using data from repositories, not directly in the mapper
    }

    /**
     * Updates an existing refund entity with data from the DTO
     * @param refundDTO source DTO with updated data
     * @param refund existing entity to update
     */
    @Mapping(target = "claim", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromDto(RefundDTO refundDTO, @MappingTarget Refund refund);

    /**
     * Updates entity with relationship objects
     * @param refund entity to update
     * @param claim claim to associate
     * @param user user to associate
     */
    @Named("withRelationships")
    default Refund withRelationships(Refund refund, Claim claim, User user) {
        refund.setClaim(claim);
        refund.setUser(user);
        return refund;
    }
}