package org.example.medinsurance;

import org.example.medinsurance.dto.UpdateProfileRequest;
import org.example.medinsurance.dto.UserDTO;
import org.example.medinsurance.enums.Role;
import org.example.medinsurance.model.User;
import org.example.medinsurance.repository.UserRepository;
import org.example.medinsurance.service.FileStorageService;
import org.example.medinsurance.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private FileStorageService fileStorageService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;
    private UserDTO testUserDTO;
    private UpdateProfileRequest updateProfileRequest;

    @BeforeEach
    void setUp() {
        // Initialize test user
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");
        testUser.setPassword("encoded_password");
        testUser.setBirthday(LocalDate.of(1990, 1, 1));
        testUser.setAddress("123 Test St");
        testUser.setPhone("1234567890");
        testUser.setEnabled(true);
        testUser.setRole(Role.CLIENT);

        // Initialize test user DTO
        testUserDTO = new UserDTO();
        testUserDTO.setId(1L);
        testUserDTO.setName("Test User");
        testUserDTO.setEmail("test@example.com");
        testUserDTO.setPassword("password123");
        testUserDTO.setBirthday(LocalDate.of(1990, 1, 1));
        testUserDTO.setAddress("123 Test St");
        testUserDTO.setPhone("1234567890");
        testUserDTO.setRole(Role.CLIENT);

        // Initialize update profile request
        updateProfileRequest = new UpdateProfileRequest();
        updateProfileRequest.setName("Updated Name");
        updateProfileRequest.setEmail("updated@example.com");
        updateProfileRequest.setBirthday(LocalDate.of(1991, 2, 2));
        updateProfileRequest.setAddress("456 Update St");
        updateProfileRequest.setPhone("0987654321");
        updateProfileRequest.setPassword("newpassword123");
        updateProfileRequest.setImage("base64encodedimage");
    }

    @Test
    void getAuthenticatedUser_ShouldReturnUser() {
        // Set up Security Context (moved from setUp to only tests that need it)
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("test@example.com");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        // Act
        User result = userService.getAuthenticatedUser();

        // Assert
        assertNotNull(result);
        assertEquals(testUser.getId(), result.getId());
        assertEquals(testUser.getEmail(), result.getEmail());
        verify(userRepository).findByEmail("test@example.com");
    }

    @Test
    void getAuthenticatedUser_WithInvalidEmail_ShouldThrowException() {
        // Set up Security Context
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("test@example.com");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            userService.getAuthenticatedUser();
        });

        assertEquals("User not found", exception.getMessage());
        verify(userRepository).findByEmail("test@example.com");
    }

    @Test
    void createUser_ShouldReturnCreatedUser() {
        // Arrange - only mock what will be used
        when(passwordEncoder.encode(testUserDTO.getPassword())).thenReturn("encoded_password");

        // Need to capture what's saved to verify the fields are set correctly
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setId(1L); // Simulate DB assigning ID
            return savedUser;
        });

        // Act
        User result = userService.createUser(testUserDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(testUserDTO.getName(), result.getName());
        assertEquals(testUserDTO.getEmail(), result.getEmail());
        assertEquals("encoded_password", result.getPassword());
        assertEquals(testUserDTO.getBirthday(), result.getBirthday());
        assertEquals(testUserDTO.getAddress(), result.getAddress());
        assertEquals(testUserDTO.getPhone(), result.getPhone());
        assertEquals(testUserDTO.getRole(), result.getRole());
        assertTrue(result.isEnabled());

        verify(passwordEncoder).encode(testUserDTO.getPassword());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateUser_ShouldReturnUpdatedUser() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.encode("newpassword")).thenReturn("new_encoded_password");

        // Need to capture the saved user to verify fields
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Create updated DTO
        UserDTO updatedDTO = new UserDTO();
        updatedDTO.setId(1L);
        updatedDTO.setName("Updated User");
        updatedDTO.setEmail("updated@example.com");
        updatedDTO.setPassword("newpassword");
        updatedDTO.setBirthday(LocalDate.of(1991, 2, 2));
        updatedDTO.setAddress("456 Updated St");
        updatedDTO.setPhone("0987654321");
        updatedDTO.setRole(Role.ADMIN);

        // Act
        User result = userService.updateUser(updatedDTO);

        // Assert
        assertNotNull(result);
        assertEquals(updatedDTO.getName(), result.getName());
        assertEquals(updatedDTO.getEmail(), result.getEmail());
        assertEquals("new_encoded_password", result.getPassword());
        assertEquals(updatedDTO.getBirthday(), result.getBirthday());
        assertEquals(updatedDTO.getAddress(), result.getAddress());
        assertEquals(updatedDTO.getPhone(), result.getPhone());
        assertEquals(updatedDTO.getRole(), result.getRole());

        verify(userRepository).findById(1L);
        verify(passwordEncoder).encode(updatedDTO.getPassword());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateUser_WithInvalidId_ShouldThrowException() {
        // Arrange
        Long invalidId = 99L;
        when(userRepository.findById(invalidId)).thenReturn(Optional.empty());

        testUserDTO.setId(invalidId); // Invalid ID

        // Act & Assert
        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            userService.updateUser(testUserDTO);
        });

        assertEquals("User not found", exception.getMessage());
        verify(userRepository).findById(invalidId);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void updateProfile_ShouldReturnUpdatedUser() {
        // Set up Security Context
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("test@example.com");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.encode(updateProfileRequest.getPassword())).thenReturn("new_encoded_password");
        when(fileStorageService.storeBase64Image(updateProfileRequest.getImage())).thenReturn("stored_image_filename.jpg");

        // Capture saved user
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User result = userService.updateProfile(updateProfileRequest);

        // Assert
        assertNotNull(result);
        assertEquals(updateProfileRequest.getName(), result.getName());
        assertEquals(updateProfileRequest.getEmail(), result.getEmail());
        assertEquals("new_encoded_password", result.getPassword());
        assertEquals(updateProfileRequest.getBirthday(), result.getBirthday());
        assertEquals(updateProfileRequest.getAddress(), result.getAddress());
        assertEquals(updateProfileRequest.getPhone(), result.getPhone());
        assertEquals("stored_image_filename.jpg", result.getImage());

        verify(userRepository).findByEmail("test@example.com");
        verify(passwordEncoder).encode(updateProfileRequest.getPassword());
        verify(fileStorageService).storeBase64Image(updateProfileRequest.getImage());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateProfile_WithoutImage_ShouldNotCallFileStorage() {
        // Set up Security Context
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("test@example.com");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        // Create request without image
        UpdateProfileRequest requestWithoutImage = new UpdateProfileRequest();
        requestWithoutImage.setName("Updated Name");
        requestWithoutImage.setPassword("newpassword123");
        requestWithoutImage.setImage(null); // No image

        when(passwordEncoder.encode(requestWithoutImage.getPassword())).thenReturn("new_encoded_password");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User result = userService.updateProfile(requestWithoutImage);

        // Assert
        assertNotNull(result);
        assertEquals(requestWithoutImage.getName(), result.getName());
        assertEquals("new_encoded_password", result.getPassword());

        verify(userRepository).findByEmail("test@example.com");
        verify(passwordEncoder).encode(requestWithoutImage.getPassword());
        verify(fileStorageService, never()).storeBase64Image(anyString());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void getUserById_ShouldReturnUser() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // Act
        User result = userService.getUserById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(testUser.getId(), result.getId());
        assertEquals(testUser.getName(), result.getName());
        verify(userRepository).findById(1L);
    }

    @Test
    void getUserById_WithInvalidId_ShouldThrowException() {
        // Arrange
        Long invalidId = 99L;
        when(userRepository.findById(invalidId)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            userService.getUserById(invalidId);
        });

        assertEquals("User not found", exception.getMessage());
        verify(userRepository).findById(invalidId);
    }

    @Test
    void getAllUsers_ShouldReturnAllUsers() {
        // Arrange
        User user1 = new User();
        user1.setId(1L);
        user1.setName("User One");

        User user2 = new User();
        user2.setId(2L);
        user2.setName("User Two");

        List<User> users = Arrays.asList(user1, user2);

        when(userRepository.findAll()).thenReturn(users);

        // Act
        List<User> result = userService.getAllUsers();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(user1.getId(), result.get(0).getId());
        assertEquals(user2.getId(), result.get(1).getId());
        verify(userRepository).findAll();
    }

    @Test
    void toggleUserEnabled_ShouldToggleAndReturnUser() {
        // Arrange
        testUser.setEnabled(true); // Ensure we know the initial state

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // Capture the saved user to verify enabled flag was toggled
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            return savedUser;
        });

        // Act
        User result = userService.toggleUserEnabled(1L);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEnabled()); // Should be toggled from true to false

        verify(userRepository).findById(1L);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void toggleUserEnabled_WithInvalidId_ShouldThrowException() {
        // Arrange
        Long invalidId = 99L;
        when(userRepository.findById(invalidId)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            userService.toggleUserEnabled(invalidId);
        });

        assertEquals("User not found", exception.getMessage());
        verify(userRepository).findById(invalidId);
        verify(userRepository, never()).save(any(User.class));
    }
}