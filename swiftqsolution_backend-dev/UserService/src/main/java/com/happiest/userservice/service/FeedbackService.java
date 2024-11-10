package com.happiest.userservice.service;

import com.happiest.userservice.dao.FeedbackInterface;
import com.happiest.userservice.dto.Feedback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FeedbackService {
    @Autowired
    private FeedbackInterface feedbackInterface;

    // Method to submit feedback with exception handling
    public Feedback submitFeedback(Feedback feedback) {
        try {
            return feedbackInterface.save(feedback);
        } catch (Exception e) {
            // Log the error (consider using a logging framework)
            System.err.println("Error occurred while saving feedback: " + e.getMessage());
            throw new RuntimeException("Error while submitting feedback: " + e.getMessage(), e);
        }
    }

    // Method to get feedback by ID with exception handling
    public List<Feedback> getAllFeedbacks() {
        try {
            return feedbackInterface.findAll(); // Retrieves all feedbacks
        } catch (Exception e) {
            // Log the error
            System.err.println("Error occurred while retrieving all feedback: " + e.getMessage());
            throw new RuntimeException("Error while retrieving feedbacks: " + e.getMessage(), e);
        }

    }
}
