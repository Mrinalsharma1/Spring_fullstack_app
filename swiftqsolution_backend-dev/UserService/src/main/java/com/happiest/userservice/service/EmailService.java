package com.happiest.userservice.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    private static final Logger LOGGER = LogManager.getLogger(EmailService.class);

    /**
     * Sends a simple email to the provided recipient.
     *
     * @param to      Recipient's email address
     * @param subject Subject of the email
     * @param text    Body of the email
     */
    public void sendEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            message.setFrom("nousemail77@gmail.com"); // Use your email here

            mailSender.send(message);
            LOGGER.info("Email sent to: " + to);

        } catch (MailException e) {
            LOGGER.error("Error occurred while sending email to: " + to, e);
            // Optionally, rethrow the exception or handle it as per your application logic
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
