package org.example.medinsurance.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.example.medinsurance.dto.UpdateProfileRequest;
import org.example.medinsurance.dto.UserDTO;
import org.example.medinsurance.model.User;
import org.example.medinsurance.service.FileStorageService;
import org.example.medinsurance.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final FileStorageService fileStorageService;

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> addUser(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.createUser(userDTO));
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> updateUser(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.updateUser(userDTO));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PatchMapping("/{userId}/toggle-enabled")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> toggleUserEnabled(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.toggleUserEnabled(userId));
    }

    @PutMapping(value = "/profile", consumes = {"multipart/form-data"})
    public ResponseEntity<User> updateProfile(
            @RequestPart("profileData") String profileDataJson,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        try {
            // Create ObjectMapper with JSR310 module for handling LocalDate
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

            // Parse the JSON into UpdateProfileRequest
            UpdateProfileRequest request = objectMapper.readValue(profileDataJson, UpdateProfileRequest.class);

            // Handle image if provided - convert to base64 string
            if (image != null && !image.isEmpty()) {
                try {
                    byte[] imageBytes = image.getBytes();
                    String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                    request.setImage(base64Image);
                } catch (Exception e) {
                    throw new RuntimeException("Error processing image: " + e.getMessage(), e);
                }
            }

            // Process the update using the existing service method
            return ResponseEntity.ok(userService.updateProfile(request));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error updating profile: " + e.getMessage(), e);
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<User> getProfile() {
        return ResponseEntity.ok(userService.getAuthenticatedUser());
    }
}
