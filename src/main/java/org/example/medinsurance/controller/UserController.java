package org.example.medinsurance.controller;

import lombok.RequiredArgsConstructor;
import org.example.medinsurance.dto.UpdateProfileRequest;
import org.example.medinsurance.model.User;
import org.example.medinsurance.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
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

    @PutMapping("/profile")
    public ResponseEntity<User> updateProfile(@RequestBody UpdateProfileRequest request) {
        return ResponseEntity.ok(userService.updateProfile(request));
    }
}
