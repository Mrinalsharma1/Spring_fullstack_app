package com.happiest.userservice.service;

import com.happiest.userservice.dao.NewsLetterInterface;
import com.happiest.userservice.dto.NewsLetter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsLetterService {

    @Autowired
    NewsLetterInterface newsLetterInterface;

    @Autowired
    EmailService emailService;

    public String subscribeToNewsletter(String email) {
        try {
            NewsLetter newsletter = new NewsLetter();
            newsletter.setEmail(email);
            newsLetterInterface.save(newsletter); // Save the email to DB
            return "Subscription successful!";
        } catch (Exception e) {
            // Handle any errors that occur while saving
            throw new RuntimeException("Error occurred while subscribing: " + e.getMessage());
        }
    }

    public List<NewsLetter> getAllSubscriptions() {
        try {
            return newsLetterInterface.findAll();
        } catch (Exception e) {
            // Handle any errors that occur during fetching
            throw new RuntimeException("Error occurred while fetching subscriptions: " + e.getMessage());
        }
    }

    public boolean sendAnnouncementToAll(String subject, String message) {
        List<String> subscriberEmails = newsLetterInterface.findAllSubscriberEmails();

        if (subscriberEmails.isEmpty()) {
            return false; // No subscribers found
        }

        for (String email : subscriberEmails) {
            try {
                emailService.sendEmail(email, subject, message); // Send email to each subscriber
            } catch (Exception e) {
                // Log the error and continue with the next subscriber
                System.err.println("Failed to send email to: " + email + ". Error: " + e.getMessage());
                return false; // Optionally, return false if any email sending fails
            }
        }
        return true; // Return true if all emails were sent successfully
    }
}
