package org.example.medinsurance.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.medinsurance.dto.LoginUserDto;
import org.example.medinsurance.dto.RegisterUserDto;
import org.example.medinsurance.dto.VerifyUserDto;
import org.example.medinsurance.model.User;
import org.example.medinsurance.responses.LoginResponse;
import org.example.medinsurance.service.AuthenticationService;
import org.example.medinsurance.service.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.Base64;

@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    @PostMapping(value = "/signup", consumes = {"multipart/form-data"})
    public ResponseEntity<String> signup(
            @RequestPart("user") String userJson,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        try {

            // Create ObjectMapper with JSR310 module for handling LocalDate
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

            // Parse the JSON into RegisterUserDto
            RegisterUserDto registerUserDto = objectMapper.readValue(userJson, RegisterUserDto.class);

            // Handle image if provided - convert to base64 string
            if (image != null && !image.isEmpty()) {
                try {
                    byte[] imageBytes = image.getBytes();
                    String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                    registerUserDto.setImage(base64Image);
                    System.out.println("Image converted to base64 (length: " + base64Image.length() + ")");
                } catch (Exception e) {
                    System.err.println("Error processing image: " + e.getMessage());
                    e.printStackTrace();
                }
            }

            // Call the service to register the user
            User user = authenticationService.signup(registerUserDto);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("User registered successfully. Please check your email for verification.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error during registration: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody @Valid LoginUserDto loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);
        String jwtToken = jwtService.generateToken(authenticatedUser);
        LoginResponse loginResponse = new LoginResponse(jwtToken, jwtService.getExpirationTime());
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestBody @Valid VerifyUserDto verifyUserDto) {
        try {
            authenticationService.verifyUser(verifyUserDto);
            return ResponseEntity.ok("Account verified successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/resend")
    public ResponseEntity<?> resendVerificationCode(@RequestParam String email) {
        try {
            authenticationService.resendVerificationCode(email);
            return ResponseEntity.ok("Verification code sent successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
