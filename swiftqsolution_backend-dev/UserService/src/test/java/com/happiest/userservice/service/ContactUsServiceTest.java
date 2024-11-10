package com.happiest.userservice.service;

import com.happiest.userservice.dao.ContactUsInterface;
import com.happiest.userservice.dto.ContactUs;
import com.happiest.userservice.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
class ContactUsServiceTest {

    @InjectMocks
    private ContactUsService contactUsService;

    @Mock
    private ContactUsInterface contactUsInterface;

    @Mock
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddMessage_Success() {
        ContactUs contactUs = new ContactUs();
        contactUs.setEmail("test@example.com");
        contactUs.setMessage("This is a test message.");
        contactUs.setSubject("Test Subject");

        when(contactUsInterface.save(any(ContactUs.class))).thenReturn(contactUs);

        String response = contactUsService.addMessage(contactUs);

        assertEquals("Message sent successfully!", response);
        verify(contactUsInterface).save(contactUs); // Ensure save is called
    }

    @Test
    void testAddMessage_Failure() {
        ContactUs contactUs = new ContactUs();
        when(contactUsInterface.save(any(ContactUs.class))).thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            contactUsService.addMessage(contactUs);
        });

        assertEquals("Failed to send message. Please try again later.", exception.getMessage());
        verify(contactUsInterface).save(contactUs); // Ensure save is called
    }

    @Test
    void testGetAllMessages_Success() {
        ContactUs message1 = new ContactUs();
        message1.setEmail("test1@example.com");
        message1.setMessage("Message 1");
        message1.setSubject("Subject 1");

        ContactUs message2 = new ContactUs();
        message2.setEmail("test2@example.com");
        message2.setMessage("Message 2");
        message2.setSubject("Subject 2");

        when(contactUsInterface.findByStatusFalse()).thenReturn(List.of(message1, message2));

        List<ContactUs> messages = contactUsService.getAllMessages();

        assertEquals(2, messages.size());
        verify(contactUsInterface).findByStatusFalse(); // Ensure retrieval method is called
    }

    @Test
    void testGetAllMessages_EmptyList() {
        when(contactUsInterface.findByStatusFalse()).thenReturn(Collections.emptyList());

        List<ContactUs> messages = contactUsService.getAllMessages();

        assertTrue(messages.isEmpty());
        verify(contactUsInterface).findByStatusFalse(); // Ensure retrieval method is called
    }

    @Test
    void testGetAllMessages_Error() {
        when(contactUsInterface.findByStatusFalse()).thenThrow(new RuntimeException("Database error"));

        List<ContactUs> messages = contactUsService.getAllMessages();

        assertTrue(messages.isEmpty()); // Should return an empty list
        verify(contactUsInterface).findByStatusFalse(); // Ensure retrieval method is called
    }

    @Test
    void testReplyToUser_Success() {
        String email = "test@example.com";
        String replyMessage = "This is a reply.";

        ContactUs contactMessage = new ContactUs();
        contactMessage.setEmail(email);
        contactMessage.setSubject("Test Subject");
        contactMessage.setMessage("Original message");

        when(contactUsInterface.findByEmail(email)).thenReturn(Optional.of(contactMessage));
        doNothing().when(emailService).sendEmail(anyString(), anyString(), anyString());
        doNothing().when(contactUsInterface).updateStatusByEmail(email);

        boolean result = contactUsService.replyToUser(email, replyMessage);

        assertTrue(result); // Should return true
        verify(emailService).sendEmail(email, "Re: " + contactMessage.getSubject(), replyMessage); // Verify email sending
        verify(contactUsInterface).updateStatusByEmail(email); // Verify status update
    }

    @Test
    void testReplyToUser_NotFound() {
        String email = "nonexistent@example.com";
        String replyMessage = "This is a reply.";

        when(contactUsInterface.findByEmail(email)).thenReturn(Optional.empty());

        boolean result = contactUsService.replyToUser(email, replyMessage);

        assertFalse(result); // Should return false
        verify(emailService, never()).sendEmail(anyString(), anyString(), anyString()); // Verify email sending is not attempted
        verify(contactUsInterface, never()).updateStatusByEmail(email); // Ensure status update is not called
    }
}
