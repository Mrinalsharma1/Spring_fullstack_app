package com.happiest.userservice.controller;

import com.happiest.userservice.dto.ContactUs;
import com.happiest.userservice.service.ContactUsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
@ActiveProfiles("test")
class ContactUsControllerTest {

    @InjectMocks
    private ContactUsController contactUsController;

    @Mock
    private ContactUsService contactUsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSubmitContactForm_Success() {
        // Mocking the service layer
        when(contactUsService.addMessage(any(ContactUs.class))).thenReturn("Message submitted successfully!");

        // Creating a sample request body
        ContactUs contactUs = new ContactUs();
        contactUs.setEmail("test@example.com");
        contactUs.setMessage("Test message");

        // Call the controller method
        ResponseEntity<Map<String, Object>> responseEntity = contactUsController.submitContactForm(contactUs);

        // Assertions
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("success", responseEntity.getBody().get("status"));
        assertEquals("Message submitted successfully!", responseEntity.getBody().get("message"));
    }

    @Test
    void testSubmitContactForm_Failure() {
        // Mocking the service layer to throw a RuntimeException
        when(contactUsService.addMessage(any(ContactUs.class))).thenThrow(new RuntimeException("Some error occurred"));

        // Creating a sample request body
        ContactUs contactUs = new ContactUs();
        contactUs.setEmail("test@example.com");
        contactUs.setMessage("Test message");

        // Call the controller method
        ResponseEntity<Map<String, Object>> responseEntity = contactUsController.submitContactForm(contactUs);

        // Assertions
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("fail", responseEntity.getBody().get("status"));
        assertEquals("Failed to send message: Some error occurred", responseEntity.getBody().get("message"));
    }

    @Test
    void testGetAllMessages_Success() {
        // Mocking the service layer
        List<ContactUs> contactUsList = new ArrayList<>();
        ContactUs contact1 = new ContactUs();
        contact1.setEmail("test1@example.com");
        contact1.setMessage("Test message 1");

        ContactUs contact2 = new ContactUs();
        contact2.setEmail("test2@example.com");
        contact2.setMessage("Test message 2");

        contactUsList.add(contact1);
        contactUsList.add(contact2);

        when(contactUsService.getAllMessages()).thenReturn(contactUsList);

        // Call the controller method
        ResponseEntity<Map<String, Object>> responseEntity = contactUsController.getAllMessages();

        // Assertions
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("success", responseEntity.getBody().get("status"));
        assertEquals(contactUsList, responseEntity.getBody().get("messages"));
    }

    @Test
    void testGetAllMessages_Failure() {
        // Mocking the service layer to throw an exception
        when(contactUsService.getAllMessages()).thenThrow(new RuntimeException("Database error"));

        // Call the controller method
        ResponseEntity<Map<String, Object>> responseEntity = contactUsController.getAllMessages();

        // Assertions
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("fail", responseEntity.getBody().get("status"));
        assertEquals("Failed to retrieve messages: Database error", responseEntity.getBody().get("message"));
    }

    @Test
    void testReplyToUser_Success() {
        // Mocking the service layer
        when(contactUsService.replyToUser("test@example.com", "Reply message")).thenReturn(true);

        // Creating a sample request body
        Map<String, String> reply = new HashMap<>();
        reply.put("email", "test@example.com");
        reply.put("message", "Reply message");

        // Call the controller method
        ResponseEntity<Map<String, Object>> responseEntity = contactUsController.replyToUser(reply);

        // Assertions
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("success", responseEntity.getBody().get("status"));
        assertEquals("Reply sent successfully and status updated.", responseEntity.getBody().get("message"));
    }

    @Test
    void testReplyToUser_Failure() {
        // Mocking the service layer to return false when email is not found
        when(contactUsService.replyToUser("test@example.com", "Reply message")).thenReturn(false);

        // Creating a sample request body
        Map<String, String> reply = new HashMap<>();
        reply.put("email", "test@example.com");
        reply.put("message", "Reply message");

        // Call the controller method
        ResponseEntity<Map<String, Object>> responseEntity = contactUsController.replyToUser(reply);

        // Assertions
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("fail", responseEntity.getBody().get("status"));
        assertEquals("Contact message not found for the provided email.", responseEntity.getBody().get("message"));
    }

    @Test
    void testReplyToUser_Exception() {
        // Mocking the service layer to throw an exception
        when(contactUsService.replyToUser("test@example.com", "Reply message")).thenThrow(new RuntimeException("Some error occurred"));

        // Creating a sample request body
        Map<String, String> reply = new HashMap<>();
        reply.put("email", "test@example.com");
        reply.put("message", "Reply message");

        // Call the controller method
        ResponseEntity<Map<String, Object>> responseEntity = contactUsController.replyToUser(reply);

        // Assertions
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("fail", responseEntity.getBody().get("status"));
        assertEquals("Error occurred: Some error occurred", responseEntity.getBody().get("message"));
    }
}
