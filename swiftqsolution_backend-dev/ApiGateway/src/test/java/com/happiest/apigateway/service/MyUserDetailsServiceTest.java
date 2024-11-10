package com.happiest.apigateway.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.happiest.apigateway.model.UserPrincipal;
import com.happiest.apigateway.model.Users;
import com.happiest.apigateway.repository.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class MyUserDetailsServiceTest {

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private MyUserDetailsService myUserDetailsService;

    private Users user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new Users();
        user.setUsername("testuser");
        user.setPassword("password");
    }

    @Test
    public void testLoadUserByUsername_UserExists() {
        when(userRepo.findByUsername("testuser")).thenReturn(user);

        UserPrincipal userPrincipal = (UserPrincipal) myUserDetailsService.loadUserByUsername("testuser");

        assertNotNull(userPrincipal);
        assertEquals("testuser", userPrincipal.getUsername());
        verify(userRepo, times(1)).findByUsername("testuser");
    }

    @Test
    public void testLoadUserByUsername_UserNotFound() {
        when(userRepo.findByUsername("unknownuser")).thenReturn(null);

        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            myUserDetailsService.loadUserByUsername("unknownuser");
        });

        assertEquals("User not found", exception.getMessage());
        verify(userRepo, times(1)).findByUsername("unknownuser");
    }
}
