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

import java.util.Base64;

@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    @PostMapping(value = "/signup", consumes = {"multipart/form-data"})
    public ResponseEntity<String> signup(
            @RequestParam("user") String userJson, // The user data as a JSON string
            @RequestParam("image") MultipartFile image) { // The image file
        try {
            // You can parse the user JSON string into the RegisterUserDto (use a library like Jackson)
            RegisterUserDto registerUserDto = new ObjectMapper().readValue(userJson, RegisterUserDto.class);
            // Convert the image to a base64 string for storage
            String base64Image = Base64.getEncoder().encodeToString(image.getBytes());

            registerUserDto.setImage(base64Image); // Set the image to the DTO

            // Call the signup service method to register the user and upload the image
            User user = authenticationService.signup(registerUserDto);

            // Return a success message
            return new ResponseEntity<>("User registered successfully. Please check your email for verification.", HttpStatus.CREATED);
        } catch (Exception e) {
            // Handle error if any occurs during the signup process
            return new ResponseEntity<>("Error during registration: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
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
