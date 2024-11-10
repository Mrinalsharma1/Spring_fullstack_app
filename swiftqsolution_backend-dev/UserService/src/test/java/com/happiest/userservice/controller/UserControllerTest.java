package com.happiest.userservice.controller;

import com.happiest.userservice.constant.PredefinedConstants;
import com.happiest.userservice.dto.ResetPasswordRequest;
import com.happiest.userservice.dto.UserEntity;
import com.happiest.userservice.service.EmailService;
import com.happiest.userservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister_Success() {
        UserEntity user = new UserEntity();
        user.setUsername("test@example.com");
        user.setPassword("password");

        when(userService.register(any(UserEntity.class))).thenReturn(user);

        ResponseEntity<?> response = userController.register(user);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals("success", body.get("status"));
        assertEquals(PredefinedConstants.RESPONSE_MESSAGE_SUCCESS_REGISTER, body.get("message"));
    }

//    hey i am adding

    @Test
    void testRegister_Failure() {
        UserEntity user = new UserEntity();
        user.setUsername("test@example.com");
        user.setPassword("password");

        when(userService.register(any(UserEntity.class))).thenThrow(new RuntimeException("Registration failed"));

        ResponseEntity<?> response = userController.register(user);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals("fail", body.get("status"));
        assertEquals(PredefinedConstants.RESPONSE_MESSAGE_FAIL_REGISTER, body.get("message"));
    }

    @Test
    void testForgotPassword_UserNotFound() {
        String email = "test@example.com";

        when(userService.findByUsername(email)).thenReturn(null);

        ResponseEntity<?> response = userController.forgotPassword(email);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals("fail", body.get("status"));
        assertEquals(PredefinedConstants.USER_NOT_REGISTERED, body.get("message"));
    }

    @Test
    void testForgotPassword_Success() {
        String email = "test@example.com";
        UserEntity user = new UserEntity();
        user.setUsername(email);
        user.setResetToken(null);
        user.setResetTokenExpiry(null);

        when(userService.findByUsername(email)).thenReturn(user);
        doNothing().when(emailService).sendEmail(anyString(), anyString(), anyString());

        ResponseEntity<?> response = userController.forgotPassword(email);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals("success", body.get("status"));
        assertEquals(PredefinedConstants.RESPONSE_MESSAGE_PASSWORD_LINK, body.get("message"));

        verify(userService).save(user); // Ensure save is called
        verify(emailService).sendEmail(anyString(), anyString(), anyString()); // Ensure email is sent
    }

    @Test
    void testResetPassword_Exception() {
        ResetPasswordRequest request = new ResetPasswordRequest("invalid-token", "newPassword");
        request.setToken("invalidToken");
        request.setNewPassword("newPassword");

        // Simulate an exception being thrown by the userService
        doThrow(new RuntimeException("Database error")).when(userService).findByResetToken(anyString());

        ResponseEntity<?> response = userController.resetPassword(request);

        Map<String, Object> expectedResponseBody = new HashMap<>();
        expectedResponseBody.put("message", "Error occurred while resetting password");
        expectedResponseBody.put("status", "fail");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(expectedResponseBody, response.getBody());
    }


    @Test
    void testForgotPassword_Exception() {
        String email = "test@example.com";

        // Simulate an exception being thrown by the userService
        doThrow(new RuntimeException("Database error")).when(userService).findByUsername(anyString());

        ResponseEntity<?> response = userController.forgotPassword(email);

        Map<String, Object> expectedResponseBody = new HashMap<>();
        expectedResponseBody.put("message", PredefinedConstants.RESPONSE_MESSAGE_FAIL_FORGOT_PASSWORD);
        expectedResponseBody.put("status", "fail");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(expectedResponseBody, response.getBody());
    }


    @Test
    void testResetPassword_InvalidToken() {
        // Create a new ResetPasswordRequest with the required arguments
        ResetPasswordRequest request = new ResetPasswordRequest("invalid-token", "newPassword");

        // Mocking the service call
        when(userService.findByResetToken(request.getToken())).thenReturn(null);

        // Call the resetPassword method
        ResponseEntity<?> response = userController.resetPassword(request);

        // Verify the response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals("fail", body.get("status"));
        assertEquals("Invalid reset token or no expiry set", body.get("message"));
    }


    @Test
    void testResetPassword_TokenExpired() {
        String token = "valid-token";
        String newPassword = "newPassword";
        ResetPasswordRequest request = new ResetPasswordRequest(token, newPassword); // Use constructor with parameters

        UserEntity user = new UserEntity();
        user.setResetToken(request.getToken());
        user.setResetTokenExpiry(LocalDateTime.now().minusMinutes(1)); // Token is expired

        when(userService.findByResetToken(request.getToken())).thenReturn(user);

        ResponseEntity<?> response = userController.resetPassword(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals("fail", body.get("status"));
        assertEquals("Reset token has expired", body.get("message"));
    }

    @Test
    void testUpdateUserProfile_Exception() {
        Long userId = 1L;
        Map<String, String> updates = new HashMap<>();
        updates.put("profilename", "newProfile");
        updates.put("email", "newEmail@example.com");

        // Simulate an exception being thrown by the userService
        doThrow(new RuntimeException("Database error")).when(userService).updateUserProfile(anyLong(), anyString(), anyString());

        ResponseEntity<Map<String, Object>> response = userController.updateUserProfile(userId, updates);

        Map<String, Object> expectedResponseBody = new HashMap<>();
        expectedResponseBody.put("status", "fail");
        expectedResponseBody.put("message", PredefinedConstants.LOGGER_REGISTER_UPDATE_ERROR2 + "Database error");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(expectedResponseBody, response.getBody());
    }



    @Test
    void testResetPassword_Success() {
        // Create the ResetPasswordRequest using the parameterized constructor
        ResetPasswordRequest request = new ResetPasswordRequest("valid-token", "newPassword");

        UserEntity user = new UserEntity();
        user.setResetToken(request.getToken());
        user.setResetTokenExpiry(LocalDateTime.now().plusMinutes(10)); // Token is valid

        when(userService.findByResetToken(request.getToken())).thenReturn(user);
        when(userService.save(any(UserEntity.class))).thenReturn(user);

        ResponseEntity<?> response = userController.resetPassword(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals("success", body.get("status"));
        assertEquals("Password successfully reset", body.get("message"));

        verify(userService).save(user); // Ensure save is called
        // Note: Here you should also compare the hashed password, not the plain one.
        assertTrue(new BCryptPasswordEncoder().matches(request.getNewPassword(), user.getPassword()));
    }


    @Test
    void testFindByUsername_Success() {
        String email = "test@example.com";
        UserEntity user = new UserEntity();
        user.setUsername(email);

        when(userService.findByUsername(email)).thenReturn(user);

        ResponseEntity<?> response = userController.findByUsername(email);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    void testFindUserById_Success() {
        Long id = 1L;
        UserEntity user = new UserEntity();
        user.setId(id);

        when(userService.findUserById(id)).thenReturn(user);

        ResponseEntity<Map<String, Object>> response = userController.findUserById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertEquals("success", body.get("status"));
        assertEquals(user, body.get("user"));
    }

    @Test
    void testFindUserById_NotFound() {
        Long id = 1L;

        when(userService.findUserById(id)).thenReturn(null);

        ResponseEntity<Map<String, Object>> response = userController.findUserById(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertEquals("fail", body.get("status"));
        assertEquals(PredefinedConstants.USER_NOT_REGISTERED + id, body.get("message"));
    }

    @Test
    void testUpdateUserProfile_Success() {
        Long id = 1L;
        Map<String, String> updates = new HashMap<>();
        updates.put("profilename", "New Name");
        updates.put("email", "new@example.com");

        UserEntity updatedUser = new UserEntity();
        updatedUser.setUsername("new@example.com");

        when(userService.updateUserProfile(id, updates.get("profilename"), updates.get("email"))).thenReturn(updatedUser);

        ResponseEntity<Map<String, Object>> response = userController.updateUserProfile(id, updates);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertEquals("success", body.get("status"));
        assertEquals(updatedUser, body.get("user"));
    }

    @Test
    void testUpdateUserProfile_NotFound() {
        Long id = 1L;
        Map<String, String> updates = new HashMap<>();
        updates.put("profilename", "New Name");
        updates.put("email", "new@example.com");

        when(userService.updateUserProfile(id, updates.get("profilename"), updates.get("email"))).thenReturn(null);

        ResponseEntity<Map<String, Object>> response = userController.updateUserProfile(id, updates);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertEquals("fail", body.get("status"));
        assertEquals(PredefinedConstants.USER_NOT_REGISTERED + id, body.get("message"));
    }

    // Add more test cases if needed
}


