package com.happiest.apigateway.filter;

import com.happiest.apigateway.service.JWTService;
import com.happiest.apigateway.service.MyUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
public class JWTFilterTest {

    @Mock
    private JWTService jwtService;

    @Mock
    private ApplicationContext context;

    @Mock
    private MyUserDetailsService myUserDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private JWTFilter jwtFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDoFilterInternal_ValidToken() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer validToken");
        when(jwtService.extractUserName("validToken")).thenReturn("testUser");
        when(context.getBean(MyUserDetailsService.class)).thenReturn(myUserDetailsService);
        when(myUserDetailsService.loadUserByUsername("testUser")).thenReturn(userDetails);
        when(jwtService.validateToken("validToken", userDetails)).thenReturn(true);

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(context).getBean(MyUserDetailsService.class);
        verify(myUserDetailsService).loadUserByUsername("testUser");
        verify(jwtService).validateToken("validToken", userDetails);
        verify(filterChain).doFilter(request, response);
        assert SecurityContextHolder.getContext().getAuthentication() instanceof UsernamePasswordAuthenticationToken;
    }

    @Test
    void testDoFilterInternal_InvalidToken() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer invalidToken");
        when(jwtService.extractUserName("invalidToken")).thenReturn("testUser");
        when(context.getBean(MyUserDetailsService.class)).thenReturn(myUserDetailsService);
        when(myUserDetailsService.loadUserByUsername("testUser")).thenReturn(userDetails);
        when(jwtService.validateToken("invalidToken", userDetails)).thenReturn(false);

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(context).getBean(MyUserDetailsService.class);
        verify(myUserDetailsService).loadUserByUsername("testUser");
        verify(jwtService).validateToken("invalidToken", userDetails);
        verify(filterChain).doFilter(request, response);
        assert SecurityContextHolder.getContext().getAuthentication() == null;
    }

    @Test
    void testDoFilterInternal_NoToken() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assert SecurityContextHolder.getContext().getAuthentication() == null;
    }

    @Test
    void testDoFilterInternal_Exception() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer validToken");
        when(jwtService.extractUserName("validToken")).thenThrow(new RuntimeException("JWT error"));

        assertThrows(RuntimeException.class, () -> {
            jwtFilter.doFilterInternal(request, response, filterChain);
        });

        verify(jwtService).extractUserName("validToken");
        assert SecurityContextHolder.getContext().getAuthentication() == null;
    }


}
