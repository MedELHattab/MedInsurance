package org.example.medinsurance.service;

import org.example.medinsurance.dto.UpdateProfileRequest;
import org.example.medinsurance.model.User;

public interface UserService {
    User getAuthenticatedUser();
    User updateProfile(UpdateProfileRequest request);
}
