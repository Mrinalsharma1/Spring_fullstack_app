package com.happiest.apigateway.apigateway.feedbackservice.controller;

import com.happiest.apigateway.apigateway.feedbackservice.repository.FeedbackInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController

public class FeedbackController {
    @Autowired
    private FeedbackInterface feedbackInterface;
    @GetMapping("/getfeedbacks")
    public ResponseEntity<Map<String, Object>> getAllFeedbacks() {
        Map<String, Object> response = new HashMap<>();
        try {
            return feedbackInterface.getAllFeedbacks();
        } catch (Exception e) {
            response.put("status", "fail");
            response.put("message", "" + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/feedback/count")
    public ResponseEntity<Map<String, Object>> countFeedbacks(@RequestParam long pincode){
        Map<String, Object> response = new HashMap<>();
        try {
            return feedbackInterface.countFeedbacks(pincode);
        } catch (Exception e) {
            response.put("status", "fail");
            response.put("message", "" + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }


    }
}

