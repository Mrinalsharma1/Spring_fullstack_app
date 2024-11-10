package com.happiest.apigateway.apigateway.feedbackservice.repository;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name="http://Feedback/feedback")
public interface FeedbackInterface {
    @GetMapping("/getfeedbacks")
    public ResponseEntity<Map<String, Object>> getAllFeedbacks();

    @GetMapping("/feedback/count")
    public ResponseEntity<Map<String, Object>> countFeedbacks(@RequestParam long pincode);
}
