package com.happiest.apigateway.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.happiest.apigateway.model.AuthResponse;
import com.happiest.apigateway.model.Users;
import com.happiest.apigateway.repository.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

public class UserServiceTest {

    @Mock
    private UserRepo userRepo;

    @Mock
    private JWTService jwtService;

    @Mock
    private AuthenticationManager authManager;

    @InjectMocks
    private UserService userService;

    private Users user;
    private Authentication authentication;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new Users();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setId(1L);
        user.setProfilename("Test User");
        user.setRole("USER");

        authentication = mock(Authentication.class);
    }

    @Test
    public void testRegister() {
        when(userRepo.save(any(Users.class))).thenReturn(user);

        Users registeredUser = userService.register(user);

        assertNotNull(registeredUser);
        assertEquals("testuser", registeredUser.getUsername());
        verify(userRepo, times(1)).save(user);
    }


    @Test
    public void testVerify_BadCredentials() {
        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        AuthResponse authResponse = userService.verify(user);

        assertNull(authResponse);
        verify(authManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    public void testVerify_NullPointerException() {
        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new NullPointerException("Null value encountered"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.verify(user);
        });

        assertEquals("Null value in user details", exception.getMessage());
        verify(authManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    public void testVerify_OtherException() {
        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Other error"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.verify(user);
        });

        assertEquals("Other error", exception.getMessage());
        verify(authManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }
}
