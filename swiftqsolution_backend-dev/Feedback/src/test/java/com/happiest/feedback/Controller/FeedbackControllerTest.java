package com.happiest.feedback.Controller;

import com.happiest.feedback.constant.PredefinedConstants;
import com.happiest.feedback.controller.FeedbackController;
import com.happiest.feedback.dto.Feedback;
import com.happiest.feedback.service.FeedbackService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FeedbackControllerTest {

    @Mock
    private FeedbackService feedbackService;

    @InjectMocks
    private FeedbackController feedbackController;

    private Feedback sampleFeedback;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize sample feedback data
        sampleFeedback = new Feedback();
        sampleFeedback.setName("John Doe");
        sampleFeedback.setEmail("john.doe@example.com");
        sampleFeedback.setPincode(560001L);
        sampleFeedback.setReview("This is a sample review.");
    }

    @Test
    void testSubmitFeedback_Success() {
        Feedback savedFeedback = new Feedback();
        savedFeedback.setFid(1L);

        when(feedbackService.submitFeedback(any(Feedback.class))).thenReturn(savedFeedback);

        ResponseEntity<Map<String, Object>> response = feedbackController.submitFeedback(sampleFeedback);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("success", response.getBody().get("status"));
        assertEquals(PredefinedConstants.FEEDBACK_ADD_INFO2, response.getBody().get("message"));
        assertEquals(savedFeedback, response.getBody().get("data"));
    }

    @Test
    void testSubmitFeedback_Exception() {
        when(feedbackService.submitFeedback(any(Feedback.class))).thenThrow(new RuntimeException("Database error"));

        ResponseEntity<Map<String, Object>> response = feedbackController.submitFeedback(sampleFeedback);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("fail", response.getBody().get("status"));
        assertEquals(PredefinedConstants.FEEDBACK_ADD_ERROR + "Database error", response.getBody().get("message"));
    }

    @Test
    void testGetAllFeedbacks_Success() {
        List<Feedback> feedbacks = List.of(sampleFeedback, new Feedback());

        when(feedbackService.getAllFeedbacks()).thenReturn(feedbacks);

        ResponseEntity<Map<String, Object>> response = feedbackController.getAllFeedbacks();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("success", response.getBody().get("status"));
        assertEquals(PredefinedConstants.FEEDBACK_FETCH_INFO2, response.getBody().get("message"));
        assertEquals(feedbacks, response.getBody().get("data"));
    }

    @Test
    void testGetAllFeedbacks_Exception() {
        when(feedbackService.getAllFeedbacks()).thenThrow(new RuntimeException("Database error"));

        ResponseEntity<Map<String, Object>> response = feedbackController.getAllFeedbacks();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("fail", response.getBody().get("status"));
        assertEquals(PredefinedConstants.FEEDBACK_FETCH_ERROR + "Database error", response.getBody().get("message"));
    }
}
