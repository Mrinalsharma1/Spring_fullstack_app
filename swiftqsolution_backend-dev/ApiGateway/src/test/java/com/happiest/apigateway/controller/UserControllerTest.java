package com.happiest.apigateway.controller;

import com.happiest.apigateway.apigateway.UserServiceInterface;
import com.happiest.apigateway.model.AuthResponse;
import com.happiest.apigateway.model.Users;
import com.happiest.apigateway.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService service;

    @Mock
    private UserServiceInterface userService;

    @Mock
    private UserServiceInterface userServiceInterface;

    private Users testUser;
    private AuthResponse authResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setting up a test user
        testUser = new Users();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setPassword("password");

        // Setting up a mock authentication response
        authResponse = new AuthResponse();
        authResponse.setId(1L);
        authResponse.setUsername("testuser");
        authResponse.setToken("some-token");
        authResponse.setProfilename("Test User");
        authResponse.setRole("USER");
    }

    @Test
    void register_shouldReturnRegisteredUser() {
        when(userService.register(any(Users.class))).thenReturn(testUser);

        Users result = userController.register(testUser);

        assertNotNull(result);
        assertEquals(testUser.getUsername(), result.getUsername());
        verify(userService, times(1)).register(testUser);
    }

    @Test
    void login_shouldReturnSuccessResponse_whenCredentialsAreValid() {
        when(service.verify(any(Users.class))).thenReturn(authResponse);

        ResponseEntity<?> response = userController.login(testUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertNotNull(responseBody);
        assertEquals("User logged in successfully", responseBody.get("message"));
        assertNotNull(responseBody.get("token"));
        verify(service, times(1)).verify(testUser);
    }

    @Test
    void login_shouldReturnUnauthorizedResponse_whenCredentialsAreInvalid() {
        when(service.verify(any(Users.class))).thenReturn(null);

        ResponseEntity<?> response = userController.login(testUser);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertNotNull(responseBody);
        assertEquals("Invalid username or password", responseBody.get("message"));
    }

    @Test
    void login_shouldReturnInternalServerError_whenExceptionOccurs() {
        when(service.verify(any(Users.class))).thenThrow(new RuntimeException("Some error"));

        ResponseEntity<?> response = userController.login(testUser);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertNotNull(responseBody);
        assertEquals("Internal Server Error", responseBody.get("message"));
    }

    @Test
    void getAllMessages_shouldReturnMessages() {
        Map<String, Object> messages = new HashMap<>();
        messages.put("msg1", "Hello World");
        when(userServiceInterface.getAllMessages()).thenReturn(new ResponseEntity<>(messages, HttpStatus.OK));

        ResponseEntity<Map<String, Object>> response = userController.getAllMessages();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(messages, response.getBody());
    }

    @Test
    void checkServiceCenter_shouldReturnCheckMessage() {
        String message = userController.checkServiceCenter();
        assertEquals("i am in check methods", message);
    }
}
