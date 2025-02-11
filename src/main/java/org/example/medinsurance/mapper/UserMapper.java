package org.example.medinsurance.mapper;

import org.example.medinsurance.dto.*;
import org.example.medinsurance.model.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // Mapping from User to UserDTO
    UserDTO toUserDTO(User user);

    // Mapping from RegisterUserDto to User
    User toUser(RegisterUserDto registerUserDto);

    // Mapping from VerifyUserDto to User (update verification details)
    void updateUserFromVerificationDto(VerifyUserDto verifyUserDto, @MappingTarget User user);

    // Mapping from LoginUserDto to User (only for login attempts)
    User toUser(LoginUserDto loginUserDto);
}
