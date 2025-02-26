package org.example.medinsurance.service;

import org.example.medinsurance.dto.UpdateProfileRequest;
import org.example.medinsurance.model.User;

import java.util.List;

public interface UserService {
    User getAuthenticatedUser();
    User updateProfile(UpdateProfileRequest request);
    User getUserById(Long userId);
    List<User> getAllUsers();
    User toggleUserEnabled(Long userId);
}
