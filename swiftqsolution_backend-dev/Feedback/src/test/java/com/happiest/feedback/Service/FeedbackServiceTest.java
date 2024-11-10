package com.happiest.feedback.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

import com.happiest.feedback.constant.PredefinedConstants;
import com.happiest.feedback.dao.FeedbackInterface;
import com.happiest.feedback.dto.Feedback;
import com.happiest.feedback.service.FeedbackService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class FeedbackServiceTest {

    @Mock
    private FeedbackInterface feedbackInterface;

    @Mock
    private Logger LOGGER;

    @InjectMocks
    private FeedbackService feedbackService;

    private Feedback feedback;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        feedback = new Feedback();
        feedback.setName("John Doe");
        feedback.setEmail("john.doe@example.com");
        feedback.setPincode(123456L);
        feedback.setReview("Great service!");
    }

    // Test for submitFeedback - success scenario
    @Test
    public void testSubmitFeedback() {
        when(feedbackInterface.save(any(Feedback.class))).thenReturn(feedback);

        Feedback savedFeedback = feedbackService.submitFeedback(feedback);

        assertNotNull(savedFeedback);
        assertEquals("John Doe", savedFeedback.getName());
        verify(feedbackInterface, times(1)).save(feedback);
    }

    // Test for submitFeedback - failure scenario
    @Test
    public void testSubmitFeedbackException() {
        when(feedbackInterface.save(any(Feedback.class))).thenThrow(new DataAccessException("Database error") {});

        Exception exception = assertThrows(RuntimeException.class, () -> {
            feedbackService.submitFeedback(feedback);
        });

        assertEquals(PredefinedConstants.FEEDBACK_ADD_ERROR, exception.getMessage());
        verify(feedbackInterface, times(1)).save(feedback);
    }


    @Test// Test for getAllFeedbacks - success scenario
    public void testGetAllFeedbacks() {
        List<Feedback> feedbackList = Arrays.asList(feedback);
        when(feedbackInterface.findAll()).thenReturn(feedbackList);

        List<Feedback> result = feedbackService.getAllFeedbacks();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
        verify(feedbackInterface, times(1)).findAll();
    }
    // Test for getAllFeedbacks - failure scenario
    @Test
    public void testGetAllFeedbacksException() {
        when(feedbackInterface.findAll()).thenThrow(new DataAccessException("Database error") {});

        Exception exception = assertThrows(RuntimeException.class, () -> {
            feedbackService.getAllFeedbacks();
        });

        assertEquals(PredefinedConstants.FEEDBACK_FETCH_ERROR, exception.getMessage());
        verify(feedbackInterface, times(1)).findAll();
    }
}