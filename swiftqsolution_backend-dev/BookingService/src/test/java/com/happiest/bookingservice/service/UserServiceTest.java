package com.happiest.bookingservice.service;

import com.happiest.bookingservice.dao.UserInterface;
import com.happiest.bookingservice.dto.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserInterface userInterface;

    @InjectMocks
    private UserService userService;

    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        userEntity = new UserEntity(1L, "John Doe", "john@example.com", "password123");
    }




    @Test
    void fetchUserById_ShouldReturnUserEntity_WhenUserExists() {
        // Arrange
        long userId = 1L;
        when(userInterface.findById(userId)).thenReturn(Optional.of(userEntity));

        // Act
        UserEntity result = userService.fetchUserById(userId);

        // Assert
        assertEquals(userEntity, result);
        verify(userInterface).findById(userId);
    }



    @Test
    void fetchUserById_ShouldThrowRuntimeException_WhenDataAccessExceptionOccurs() {
        // Arrange
        long userId = 1L;
        when(userInterface.findById(userId)).thenThrow(new DataAccessException("Database error") {});

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.fetchUserById(userId));
        assertEquals("Failed to fetch user data due to data access error", exception.getMessage());
        verify(userInterface).findById(userId);
    }

    @Test
    void fetchUserById_ShouldThrowRuntimeException_WhenUnexpectedErrorOccurs() {
        // Arrange
        long userId = 1L;
        when(userInterface.findById(userId)).thenThrow(new RuntimeException("Unexpected error"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.fetchUserById(userId));
        assertEquals("Failed to fetch user data", exception.getMessage());
        verify(userInterface).findById(userId);
    }
}
