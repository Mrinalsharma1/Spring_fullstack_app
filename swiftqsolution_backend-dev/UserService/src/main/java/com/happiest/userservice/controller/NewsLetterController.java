package com.happiest.userservice.controller;

import com.happiest.userservice.dto.NewsLetter;
import com.happiest.userservice.service.NewsLetterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/newsletter")
@RestController
@CrossOrigin
public class NewsLetterController {
    @Autowired
    private NewsLetterService newsLetterService;

    @PostMapping("/subscribe")
    public ResponseEntity<Map<String, String>> subscribeToNewsletter(@RequestBody Map<String, String> request) {
        Map<String, String> response = new HashMap<>();
        String email = request.get("email");

        try {
            String message = newsLetterService.subscribeToNewsletter(email);
            response.put("status", "success");
            response.put("message", message);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("status", "fail");
            response.put("message", "Error occurred: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/subscriptions")
    public ResponseEntity<Map<String, Object>> getAllSubscriptions() {
        Map<String, Object> response = new HashMap<>();

        try {
            List<NewsLetter> subscriptions = newsLetterService.getAllSubscriptions();
            response.put("status", "success");
            response.put("subscriptions", subscriptions);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("status", "fail");
            response.put("message", "Error occurred while fetching subscriptions: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/send-announcement")
    public ResponseEntity<Map<String, Object>> sendAnnouncement(@RequestBody Map<String, String> announcement) {
        Map<String, Object> response = new HashMap<>();

        String subject = announcement.get("subject");
        String message = announcement.get("message");

        boolean isSent = newsLetterService.sendAnnouncementToAll(subject, message);

        if (isSent) {
            response.put("status", "success");
            response.put("message", "Announcement sent to all subscribers.");
            return ResponseEntity.ok(response);
        } else {
            response.put("status", "fail");
            response.put("message", "Failed to send announcement.");
            return ResponseEntity.status(500).body(response);
        }
    }
}
