package com.happiest.userservice.controller;



import com.happiest.userservice.dto.NewsLetter;
import com.happiest.userservice.service.NewsLetterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
@ActiveProfiles("test")
class NewsLetterControllerTest {

    @InjectMocks
    private NewsLetterController newsLetterController;

    @Mock
    private NewsLetterService newsLetterService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSubscribeToNewsletter_Success() {
        Map<String, String> request = new HashMap<>();
        request.put("email", "test@example.com");
        String expectedMessage = "Successfully subscribed!";

        when(newsLetterService.subscribeToNewsletter("test@example.com")).thenReturn(expectedMessage);

        ResponseEntity<Map<String, String>> response = newsLetterController.subscribeToNewsletter(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("success", response.getBody().get("status"));
        assertEquals(expectedMessage, response.getBody().get("message"));
    }

    @Test
    void testSubscribeToNewsletter_Failure() {
        Map<String, String> request = new HashMap<>();
        request.put("email", "test@example.com");
        String errorMessage = "Error occurred";

        when(newsLetterService.subscribeToNewsletter("test@example.com")).thenThrow(new RuntimeException(errorMessage));

        ResponseEntity<Map<String, String>> response = newsLetterController.subscribeToNewsletter(request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("fail", response.getBody().get("status"));
        assertEquals("Error occurred: " + errorMessage, response.getBody().get("message"));
    }

//    @Test
//    void testGetAllSubscriptions_Success() {
//        List<NewsLetter> subscriptions = Arrays.asList(new NewsLetter("test1@example.com"), new NewsLetter("test2@example.com"));
//
//        when(newsLetterService.getAllSubscriptions()).thenReturn(subscriptions);
//
//        ResponseEntity<Map<String, Object>> response = newsLetterController.getAllSubscriptions();
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals("success", response.getBody().get("status"));
//        assertEquals(subscriptions, response.getBody().get("subscriptions"));
//    }

    @Test
    void testGetAllSubscriptions_Failure() {
        String errorMessage = "Error occurred while fetching subscriptions";

        when(newsLetterService.getAllSubscriptions()).thenThrow(new RuntimeException(errorMessage));

        ResponseEntity<Map<String, Object>> response = newsLetterController.getAllSubscriptions();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("fail", response.getBody().get("status"));
        assertEquals("Error occurred while fetching subscriptions: " + errorMessage, response.getBody().get("message"));
    }

    @Test
    void testSendAnnouncement_Success() {
        Map<String, String> announcement = new HashMap<>();
        announcement.put("subject", "Subject");
        announcement.put("message", "Message");

        when(newsLetterService.sendAnnouncementToAll("Subject", "Message")).thenReturn(true);

        ResponseEntity<Map<String, Object>> response = newsLetterController.sendAnnouncement(announcement);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("success", response.getBody().get("status"));
        assertEquals("Announcement sent to all subscribers.", response.getBody().get("message"));
    }

    @Test
    void testSendAnnouncement_Failure() {
        Map<String, String> announcement = new HashMap<>();
        announcement.put("subject", "Subject");
        announcement.put("message", "Message");

        when(newsLetterService.sendAnnouncementToAll("Subject", "Message")).thenReturn(false);

        ResponseEntity<Map<String, Object>> response = newsLetterController.sendAnnouncement(announcement);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("fail", response.getBody().get("status"));
        assertEquals("Failed to send announcement.", response.getBody().get("message"));
    }
}

