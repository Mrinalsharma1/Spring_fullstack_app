package com.happiest.userservice.controller;

import com.happiest.userservice.constant.PredefinedConstants;
import com.happiest.userservice.dto.ResetPasswordRequest;
import com.happiest.userservice.dto.UserEntity;
import com.happiest.userservice.service.EmailService;
import com.happiest.userservice.service.UserService;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/user")
@CrossOrigin
@Tag(name="UserMicroService",description="Operations related to userservice testing")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;


    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);


    /**
     *
     * @param user is an object of type UserEntity
     * @return
     */

    @Operation(summary="Register the user")
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserEntity user) {
        try {
            // Encode the password
            user.setPassword(encoder.encode(user.getPassword()));

            // Register the user
            UserEntity savedUser = userService.register(user);

            // Create a custom response body with a success message
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", PredefinedConstants.RESPONSE_MESSAGE_SUCCESS_REGISTER);
            responseBody.put("status", "success"); // This line ensures status is included
            responseBody.put("user", savedUser);

            // Return the response entity with CREATED status and the response body
            return new ResponseEntity<>(responseBody, HttpStatus.CREATED);

        } catch (Exception e) {
            // Log the exception and return an error response
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message",PredefinedConstants.RESPONSE_MESSAGE_FAIL_REGISTER);
            responseBody.put("status", "fail"); // Ensure status is included in error response

            return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam("email") String email) {
        System.out.println("email: " + email);
        try {
            // Check if user exists by username (email)
            UserEntity user = userService.findByUsername(email);
            if (user == null) {
                Map<String, Object> responseBody = new HashMap<>();
                responseBody.put("message", PredefinedConstants.USER_NOT_REGISTERED);
                responseBody.put("status", "fail");
                return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
            }

            // Generate a reset token
            String resetToken = UUID.randomUUID().toString();
            user.setResetToken(resetToken);

            // Set the reset token expiry to 15 minutes from now
            LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(15);
            user.setResetTokenExpiry(expiryTime);

            userService.save(user);

            // Create a password reset URL
            String resetUrl = "http://localhost:3000/resetpassword?token=" + resetToken;

            // Send the reset link via email by
            emailService.sendEmail(user.getUsername(), "Password Reset Request",
                    "Click the link to reset your password: " + resetUrl);

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", PredefinedConstants.RESPONSE_MESSAGE_PASSWORD_LINK);
            responseBody.put("status", "success");

            return new ResponseEntity<>(responseBody, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", PredefinedConstants.RESPONSE_MESSAGE_FAIL_FORGOT_PASSWORD);
            responseBody.put("status", "fail");
            return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        try {
            // Validate reset token
            UserEntity user = userService.findByResetToken(request.getToken());
            if (user == null || user.getResetTokenExpiry() == null) {
                Map<String, Object> responseBody = new HashMap<>();
                responseBody.put("message", "Invalid reset token or no expiry set");
                responseBody.put("status", "fail");
                return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
            }

            // Check if the reset token has expired
            if (user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
                Map<String, Object> responseBody = new HashMap<>();
                responseBody.put("message", "Reset token has expired");
                responseBody.put("status", "fail");
                return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
            }

            // Encode and update the new password
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setPassword(encoder.encode(request.getNewPassword()));

            // Clear the reset token and expiry
            user.setResetToken(null);
            user.setResetTokenExpiry(null);
            userService.save(user);

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Password successfully reset");
            responseBody.put("status", "success");

            return new ResponseEntity<>(responseBody, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace(); // Log the exception
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Error occurred while resetting password");
            responseBody.put("status", "fail");
            return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/findByUsername/{email}")
    public ResponseEntity<?> findByUsername(@PathVariable String email) {

        UserEntity user = userService.findByUsername(email);

        return new ResponseEntity<>(user, HttpStatus.OK);

    }

    @GetMapping("/findUserById/{id}")
    public ResponseEntity<Map<String, Object>> findUserById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        try {
            UserEntity user = userService.findUserById(id);

            if (user != null) {
                response.put("status", "success");
                response.put("user", user);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("status", "fail");
                response.put("message", PredefinedConstants.USER_NOT_REGISTERED+ id);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            response.put("status", "fail");
            response.put("message", PredefinedConstants.LOGGER_REGISTER_FETCH_ERRO2 + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updateuser/{id}")
    public ResponseEntity<Map<String, Object>> updateUserProfile(@PathVariable Long id, @RequestBody Map<String, String> updates) {
        Map<String, Object> response = new HashMap<>();

        try {
            String newProfileName = updates.get("profilename");
            String newEmail = updates.get("email");

            UserEntity updatedUser = userService.updateUserProfile(id, newProfileName, newEmail);

            if (updatedUser != null) {
                response.put("status", "success");
                response.put("user", updatedUser);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("status", "fail");
                response.put("message", PredefinedConstants.USER_NOT_REGISTERED + id);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            response.put("status", "fail");
            response.put("message", PredefinedConstants.LOGGER_REGISTER_UPDATE_ERROR2 + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/users/count")
    public ResponseEntity<Map<String, Object>> countUsers(@RequestParam(defaultValue = "user") String role) {
        Map<String, Object> responseBody = new HashMap<>();

        try {
            long count = userService.countUsersByRole(role);
            responseBody.put("count", count);
            responseBody.put("message", "User count retrieved successfully");
            responseBody.put("status", "success");
            return ResponseEntity.ok(responseBody);
        } catch (RuntimeException e) {
            responseBody.put("message", "An error occurred: " + e.getMessage());
            responseBody.put("status", "fail");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
        }
    }

    @GetMapping("/managers/count")
    public ResponseEntity<Map<String, Object>> countManagers(@RequestParam(defaultValue = "manager") String role) {
        Map<String, Object> responseBody = new HashMap<>();

        try {
            long count = userService.countManagersByRole(role);
            responseBody.put("count", count);
            responseBody.put("message", "Manager count retrieved successfully");
            responseBody.put("status", "success");
            return ResponseEntity.ok(responseBody);
        } catch (RuntimeException e) {
            responseBody.put("message", "An error occurred: " + e.getMessage());
            responseBody.put("status", "fail");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
        }
    }


}

