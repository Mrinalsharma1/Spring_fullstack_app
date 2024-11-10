package com.happiest.userservice.service;

import com.happiest.userservice.dao.ContactUsInterface;
import com.happiest.userservice.dto.ContactUs;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ContactUsService {

    @Autowired
    private ContactUsInterface contactUsInterface;

    @Autowired
    private EmailService emailService;

    public String addMessage(ContactUs contactUs) {
        try {
            contactUsInterface.save(contactUs);
            return "Message sent successfully!";
        } catch (Exception e) {
            // Log the exception (you can use a logger here)
            System.err.println("Error saving contact message: " + e.getMessage());
            throw new RuntimeException("Failed to send message. Please try again later.");
        }
    }

    public List<ContactUs> getAllMessages() {
        try {
            // Retrieve messages with status false
            return contactUsInterface.findByStatusFalse();
        } catch (Exception e) {
            // Log the exception (optional, but recommended)
            // logger.error("Error retrieving messages", e);
            // Return an empty list in case of error
            return new ArrayList<>(); // Return an empty list in case of error
        }
    }

    @Transactional // Ensures that both email sending and status update happen within a transaction
    public boolean replyToUser(String email, String replyMessage) {
        Optional<ContactUs> contactMessageOpt = contactUsInterface.findByEmail(email);

        if (contactMessageOpt.isPresent()) {
            ContactUs contactMessage = contactMessageOpt.get();

            // Send the reply email
            emailService.sendEmail(contactMessage.getEmail(), "Re: " + contactMessage.getSubject(), replyMessage);

            // Update the status to true after successful email sending
            contactUsInterface.updateStatusByEmail(email);

            return true; // Return true if everything succeeded
        } else {
            return false; // Return false if no contact message found
        }
    }



}
