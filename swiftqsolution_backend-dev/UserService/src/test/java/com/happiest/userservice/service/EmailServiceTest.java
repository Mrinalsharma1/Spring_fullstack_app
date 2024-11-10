package com.happiest.userservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
class EmailServiceTest {

    @InjectMocks
    private EmailService emailService;

    @Mock
    private JavaMailSender mailSender;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendEmail_Success() {
        String to = "test@example.com";
        String subject = "Test Subject";
        String text = "This is a test email.";

        emailService.sendEmail(to, subject, text);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        message.setFrom("nousemail77@gmail.com");

        // Verify that the mailSender's send method was called with the correct message
        verify(mailSender).send(message);
    }

    @Test
    void testSendEmail_Failure() {
        String to = "test@example.com";
        String subject = "Test Subject";
        String text = "This is a test email.";

        // Configure mailSender to throw a MailException when send is called
        doThrow(new MailException("Mail sending failed") {}).when(mailSender).send(any(SimpleMailMessage.class));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            emailService.sendEmail(to, subject, text);
        });

        assertEquals("Failed to send email", exception.getMessage());
        // Verify that mailSender's send method was called
        verify(mailSender).send(any(SimpleMailMessage.class));
    }
}
