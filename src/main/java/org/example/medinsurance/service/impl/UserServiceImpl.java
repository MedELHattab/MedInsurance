package org.example.medinsurance.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.medinsurance.dto.UpdateProfileRequest;
import org.example.medinsurance.model.User;
import org.example.medinsurance.repository.UserRepository;
import org.example.medinsurance.service.FileStorageService;
import org.example.medinsurance.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileStorageService fileStorageService;

    @Override
    public User getAuthenticatedUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public User updateProfile(UpdateProfileRequest request) {
        User user = getAuthenticatedUser();

        if (request.getName() != null) user.setName(request.getName());

        if (request.getEmail() != null) user.setEmail(request.getEmail());
        if (request.getPassword() != null) user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Handle profile image update (Base64)
        if (request.getImage() != null && !request.getImage().isEmpty()) {
            String storedImageFilename = fileStorageService.storeBase64Image(request.getImage());
            user.setImage(storedImageFilename);
        }

        return userRepository.save(user);
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User toggleUserEnabled(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        user.setEnabled(!user.isEnabled()); // Toggle enabled status
        return userRepository.save(user);
    }
}
