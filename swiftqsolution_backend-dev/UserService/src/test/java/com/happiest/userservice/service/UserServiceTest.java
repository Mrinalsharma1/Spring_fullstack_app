package com.happiest.userservice.service;

import com.happiest.userservice.dao.UserInterface;
import com.happiest.userservice.dto.UserEntity;
import com.happiest.userservice.exception.UserAlreadyRegistered;
import com.happiest.userservice.exception.UserNotRegistered;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.util.MissingResourceException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserInterface userInterface;

    private UserEntity user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new UserEntity();
        user.setId(1L);
        user.setUsername("test@example.com");
        user.setProfilename("Test User");
    }

//    @Test
//    void testRegister_UserAlreadyExists() {
//        when(userInterface.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
//
//        assertThrows(UserAlreadyRegistered.class, () -> userService.register(user));
//
//        verify(userInterface, times(1)).findByUsername(user.getUsername());
//    }


    @Test
    void testRegister_UserSuccessfullyRegistered() {
        when(userInterface.findByUsername(user.getUsername())).thenReturn(Optional.empty());
        when(userInterface.save(user)).thenReturn(user);

        UserEntity registeredUser = userService.register(user);

        assertNotNull(registeredUser);
        assertEquals(user.getUsername(), registeredUser.getUsername());
        verify(userInterface, times(1)).findByUsername(user.getUsername());
        verify(userInterface, times(1)).save(user);
    }

    @Test
    void testFindByUsername_UserExists() {
        when(userInterface.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        UserEntity foundUser = userService.findByUsername(user.getUsername());

        assertNotNull(foundUser);
        assertEquals(user.getUsername(), foundUser.getUsername());
        verify(userInterface, times(1)).findByUsername(user.getUsername());
    }

    @Test
    void testFindByUsername_UserDoesNotExist() {
        when(userInterface.findByUsername(user.getUsername())).thenReturn(Optional.empty());

        UserEntity foundUser = userService.findByUsername(user.getUsername());

        assertNull(foundUser);
        verify(userInterface, times(1)).findByUsername(user.getUsername());
    }

    @Test
    void testFindUserById_UserExists() {
        when(userInterface.findById(1L)).thenReturn(Optional.of(user));

        UserEntity foundUser = userService.findUserById(1L);

        assertNotNull(foundUser);
        assertEquals(user.getId(), foundUser.getId());
        verify(userInterface, times(1)).findById(1L);
    }

//    @Test
//    void testFindUserById_UserDoesNotExist() {
//        Long userId = 1L; // ID of the user to find
//        when(userInterface.findById(userId)).thenReturn(Optional.empty()); // Mock the response
//
//        // Assert that the UserNotRegistered exception is thrown
//        UserNotRegistered exception = assertThrows(UserNotRegistered.class, () -> userService.findUserById(userId));
//
//        // Optional: Check the exception message for more specific validation
//        assertEquals("User not registered.", exception.getMessage()); // Adjust based on actual implementation
//
//        // Verify that the userInterface.findById method was called exactly once
//        verify(userInterface, times(1)).findById(userId);
//    }
//
//
//    @Test
//    void testUpdateUserProfile_UserExists() {
//        when(userInterface.findById(1L)).thenReturn(Optional.of(user));
//        String newProfileName = "Updated User";
//        String newEmail = "updated@example.com";
//
//        UserEntity updatedUser = userService.updateUserProfile(1L, newProfileName, newEmail);
//
//        assertEquals(newProfileName, updatedUser.getProfilename());
//        assertEquals(newEmail, updatedUser.getUsername());
//        verify(userInterface, times(1)).save(user);
//    }
//
//    @Test
//    void testUpdateUserProfile_UserDoesNotExist() {
//        when(userInterface.findById(1L)).thenReturn(Optional.empty());
//
//        assertThrows(UserNotRegistered.class, () -> userService.updateUserProfile(1L, "New Name", "new@example.com"));
//        verify(userInterface, times(1)).findById(1L);
//    }

    @Test
    void testUpdateUserProfile_NoUpdatesMade() {
        when(userInterface.findById(1L)).thenReturn(Optional.of(user));

        UserEntity updatedUser = userService.updateUserProfile(1L, null, null);

        assertEquals(user.getProfilename(), updatedUser.getProfilename());
        verify(userInterface, never()).save(user); // Ensure save is not called if no updates are made
    }

    @Test
    void testSave_User() {
        when(userInterface.save(user)).thenReturn(user);

        UserEntity savedUser = userService.save(user);

        assertNotNull(savedUser);
        assertEquals(user.getUsername(), savedUser.getUsername());
        verify(userInterface, times(1)).save(user);
    }
}