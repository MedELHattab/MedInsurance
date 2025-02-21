package org.example.medinsurance.service.impl;

import org.example.medinsurance.dto.UpdateProfileRequest;
import org.example.medinsurance.model.User;
import org.example.medinsurance.repository.UserRepository;
import org.example.medinsurance.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User getAuthenticatedUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public User updateProfile(UpdateProfileRequest request) {
        User user = getAuthenticatedUser();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        if (request.getImage() != null) {
            user.setImage(request.getImage());
        }

        return userRepository.save(user);
    }
}
