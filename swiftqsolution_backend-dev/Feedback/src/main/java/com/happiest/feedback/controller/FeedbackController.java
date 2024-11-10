package com.happiest.feedback.controller;

import com.happiest.feedback.constant.PredefinedConstants;
import com.happiest.feedback.dto.Feedback;
import com.happiest.feedback.exceptions.PincodeNotFoundException;
import com.happiest.feedback.service.FeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name="FeedbackMicroService",description="Operations related to feedbackservice testing")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;
    /**
     *
     * @param feedback is an object of type Feedback
     * @return
     */

    // Endpoint to submit feedback
    @Operation(summary="Submitting the feedback")
    @PostMapping("/submitfeedback")
    public ResponseEntity<Map<String, Object>> submitFeedback(@RequestBody Feedback feedback) {
        Map<String, Object> response = new HashMap<>();
        try {
            Feedback savedFeedback = feedbackService.submitFeedback(feedback);
            response.put("status", "success");
            response.put("message", PredefinedConstants.FEEDBACK_ADD_INFO2);
            response.put("data", savedFeedback);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            response.put("status", "fail");
            response.put("message", PredefinedConstants.FEEDBACK_ADD_ERROR+ e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Optional: Endpoint to retrieve feedback by ID
    @GetMapping("/getfeedbacks")
    public ResponseEntity<Map<String, Object>> getAllFeedbacks() {
        Map<String, Object> responseBody = new HashMap<>();
        try {
            List<Feedback> feedbacks = feedbackService.getAllFeedbacks();
            responseBody.put("message",PredefinedConstants.FEEDBACK_FETCH_INFO2);
            responseBody.put("data", feedbacks);
            responseBody.put("status", "success");
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } catch (Exception e) {
            responseBody.put("message", PredefinedConstants.FEEDBACK_FETCH_ERROR + e.getMessage());
            responseBody.put("status", "fail");
            return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/feedback/count")
    public ResponseEntity<Map<String, Object>> countFeedbacks(@RequestParam long pincode) {
        Map<String, Object> responseBody = new HashMap<>();

        try {
            long count = feedbackService.countFeedbacksByPincode(pincode);
            responseBody.put("count", count);
            responseBody.put("message", "Feedback count retrieved successfully");
            responseBody.put("status", "success");
            return ResponseEntity.ok(responseBody);
        } catch (PincodeNotFoundException e) {
            responseBody.put("message", e.getMessage());
            responseBody.put("status", "fail");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
        } catch (RuntimeException e) {
            responseBody.put("message", "An error occurred: " + e.getMessage());
            responseBody.put("status", "fail");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
        }
    }
}
