package com.happiest.userservice.service;

import com.happiest.userservice.dao.NewsLetterInterface;
import com.happiest.userservice.dto.NewsLetter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.MailSendException;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
class NewsLetterServiceTest {

    @InjectMocks
    private NewsLetterService newsLetterService;

    @Mock
    private NewsLetterInterface newsLetterInterface;

    @Mock
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSubscribeToNewsletter_Success() {
        String email = "test@example.com";

        String response = newsLetterService.subscribeToNewsletter(email);

        assertEquals("Subscription successful!", response);
        verify(newsLetterInterface).save(any(NewsLetter.class)); // Verify save method is called
    }

    @Test
    void testSubscribeToNewsletter_Failure() {
        String email = "test@example.com";

        doThrow(new RuntimeException("Database error")).when(newsLetterInterface).save(any(NewsLetter.class));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            newsLetterService.subscribeToNewsletter(email);
        });

        assertEquals("Error occurred while subscribing: Database error", exception.getMessage());
    }

    @Test
    void testGetAllSubscriptions_Success() {
        NewsLetter subscription1 = new NewsLetter();
        subscription1.setEmail("user1@example.com");

        NewsLetter subscription2 = new NewsLetter();
        subscription2.setEmail("user2@example.com");

        when(newsLetterInterface.findAll()).thenReturn(Arrays.asList(subscription1, subscription2));

        List<NewsLetter> subscriptions = newsLetterService.getAllSubscriptions();

        assertEquals(2, subscriptions.size());
        assertEquals("user1@example.com", subscriptions.get(0).getEmail());
        assertEquals("user2@example.com", subscriptions.get(1).getEmail());
    }

    @Test
    void testGetAllSubscriptions_Failure() {
        doThrow(new RuntimeException("Database error")).when(newsLetterInterface).findAll();

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            newsLetterService.getAllSubscriptions();
        });

        assertEquals("Error occurred while fetching subscriptions: Database error", exception.getMessage());
    }

    @Test
    void testSendAnnouncementToAll_NoSubscribers() {
        when(newsLetterInterface.findAllSubscriberEmails()).thenReturn(Collections.emptyList());

        boolean result = newsLetterService.sendAnnouncementToAll("Subject", "Message");

        assertFalse(result); // Should return false as there are no subscribers
        verify(emailService, never()).sendEmail(anyString(), anyString(), anyString()); // Ensure no emails are sent
    }

    @Test
    void testSendAnnouncementToAll_Success() {
        when(newsLetterInterface.findAllSubscriberEmails()).thenReturn(Arrays.asList("user1@example.com", "user2@example.com"));

        boolean result = newsLetterService.sendAnnouncementToAll("Subject", "Message");

        assertTrue(result); // Should return true as emails are sent
        verify(emailService, times(2)).sendEmail(anyString(), eq("Subject"), eq("Message")); // Ensure emails are sent twice
    }
    @Test
    void testSendAnnouncementToAll_SomeFailures() {
        when(newsLetterInterface.findAllSubscriberEmails()).thenReturn(Arrays.asList("user1@example.com", "user2@example.com"));

        // Use MailSendException which is a concrete subclass of MailException
        doThrow(new MailSendException("Mail error")).when(emailService).sendEmail("user1@example.com", "Subject", "Message");

        boolean result = newsLetterService.sendAnnouncementToAll("Subject", "Message");

        assertFalse(result); // Should return false due to email failure
        verify(emailService).sendEmail("user1@example.com", "Subject", "Message"); // Ensure the first email was attempted

    }
}