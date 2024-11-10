package com.happiest.apigateway.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JWTServiceTest {

    private JWTService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JWTService();
    }

    @Test
    void generateToken_shouldReturnValidToken() {
        String username = "testuser";
        String token = jwtService.generateToken(username);

        assertNotNull(token);
        assertTrue(token.startsWith("eyJ")); // Valid JWTs typically start with "eyJ"
    }

    @Test
    void extractUserName_shouldReturnUsernameFromToken() {
        String username = "testuser";
        String token = jwtService.generateToken(username);

        String extractedUsername = jwtService.extractUserName(token);

        assertEquals(username, extractedUsername);
    }

    @Test
    void validateToken_shouldReturnTrue_whenTokenIsValid() {
        String username = "testuser";
        String token = jwtService.generateToken(username);

        UserDetails userDetails = Mockito.mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn(username);

        boolean isValid = jwtService.validateToken(token, userDetails);

        assertTrue(isValid);
    }

//    @Test
//    void validateToken_shouldReturnFalse_whenTokenIsInvalid() {
//        String username = "testuser";
//        String token = jwtService.generateToken(username);
//
//        // Create a new token with a different secret key to simulate an invalid token
//        String invalidToken = Jwts.builder()
//                .setSubject(username)
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + 60 * 60 * 1000)) // Valid token duration
//                .signWith(SignatureAlgorithm.HS256, Base64.getDecoder().decode("different_base64_encoded_secret_key")) // Different key
//                .compact();
//
//        UserDetails userDetails = Mockito.mock(UserDetails.class);
//        when(userDetails.getUsername()).thenReturn(username);
//
//        boolean isValid = jwtService.validateToken(invalidToken, userDetails);
//
//        assertFalse(isValid);
//    }
//
//    @Test
//    void validateToken_shouldReturnFalse_whenTokenIsExpired() {
//        String username = "testuser";
//
//        // Generate a token with a short expiration time for testing
//        String token = Jwts.builder()
//                .setSubject(username)
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() - 1000)) // Expired token
//                .signWith(jwtService.getKey()) // Use the original key from jwtService
//                .compact();
//
//        UserDetails userDetails = Mockito.mock(UserDetails.class);
//        when(userDetails.getUsername()).thenReturn(username);
//
//        boolean isValid = jwtService.validateToken(token, userDetails);
//
//        assertFalse(isValid);
//    }




    @Test
    void extractExpiration_shouldReturnExpirationDate() {
        String username = "testuser";
        String token = jwtService.generateToken(username);

        Date expirationDate = jwtService.extractExpiration(token);

        assertNotNull(expirationDate);
        assertTrue(expirationDate.after(new Date())); // Ensure the expiration date is in the future
    }
}
