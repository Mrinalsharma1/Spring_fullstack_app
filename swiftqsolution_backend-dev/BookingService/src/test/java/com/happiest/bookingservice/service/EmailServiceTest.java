package com.happiest.bookingservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.*;

public class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSendEmail() {
        // Arrange
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("nousemail77@gmail.com");
        message.setTo("test@example.com");
        message.setSubject("Test Subject");
        message.setText("Test email body");

        // Act
        emailService.sendEmail("test@example.com", "Test Subject", "Test email body");

        // Assert
        verify(mailSender, times(1)).send(message);
    }
}

