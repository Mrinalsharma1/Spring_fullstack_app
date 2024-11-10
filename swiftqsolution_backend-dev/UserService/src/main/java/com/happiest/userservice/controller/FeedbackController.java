package com.happiest.userservice.controller;

import com.happiest.userservice.dto.Feedback;
import com.happiest.userservice.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/feedback")
@CrossOrigin
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    // Endpoint to submit feedback
    @PostMapping("/submitfeedback")
    public ResponseEntity<Map<String, Object>> submitFeedback(@RequestBody Feedback feedback) {
        Map<String, Object> response = new HashMap<>();
        try {
            Feedback savedFeedback = feedbackService.submitFeedback(feedback);
            response.put("status", "success");
            response.put("message", "Feedback submitted successfully");
            response.put("data", savedFeedback);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            response.put("status", "fail");
            response.put("message", "Error submitting feedback: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Optional: Endpoint to retrieve feedback by ID
    @GetMapping("/getfeedbacks")
    public ResponseEntity<Map<String, Object>> getAllFeedbacks() {
        Map<String, Object> responseBody = new HashMap<>();
        try {
            List<Feedback> feedbacks = feedbackService.getAllFeedbacks();
            responseBody.put("message", "Feedback retrieved successfully");
            responseBody.put("data", feedbacks);
            responseBody.put("status", "success");
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } catch (Exception e) {
            responseBody.put("message", "Error occurred: " + e.getMessage());
            responseBody.put("status", "fail");
            return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
