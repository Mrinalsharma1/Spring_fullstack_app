package com.happiest.userservice.controller;

import com.happiest.userservice.dto.ContactUs;
import com.happiest.userservice.service.ContactUsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/contact")
@CrossOrigin
public class ContactUsController {

    @Autowired
    private ContactUsService contactUsService;

    @PostMapping("/addmessage")
    public ResponseEntity<Map<String, Object>> submitContactForm(@RequestBody ContactUs contactUs) {
        Map<String, Object> response = new HashMap<>();
        try {
            String responseMessage = contactUsService.addMessage(contactUs);
            response.put("status", "success");
            response.put("message", responseMessage);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            response.put("status", "fail");
            response.put("message", "Failed to send message: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getallmessages")
    public ResponseEntity<Map<String, Object>> getAllMessages() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<ContactUs> messages = contactUsService.getAllMessages();
            response.put("status", "success");
            response.put("messages", messages);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "fail");
            response.put("message", "Failed to retrieve messages: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @PostMapping("/reply")
    public ResponseEntity<Map<String, Object>> replyToUser(@RequestBody Map<String, String> reply) {
        Map<String, Object> response = new HashMap<>();

        try {
            String email = reply.get("email");
            String replyMessage = reply.get("message");

            boolean isReplied = contactUsService.replyToUser(email, replyMessage);

            if (isReplied) {
                response.put("status", "success");
                response.put("message", "Reply sent successfully and status updated.");
            } else {
                response.put("status", "fail");
                response.put("message", "Contact message not found for the provided email.");
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "fail");
            response.put("message", "Error occurred: " + e.getMessage());
            return ResponseEntity.status(500).body(response); // 500 Internal Server Error
        }
    }

}