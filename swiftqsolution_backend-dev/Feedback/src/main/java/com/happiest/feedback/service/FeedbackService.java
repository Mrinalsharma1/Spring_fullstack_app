package com.happiest.feedback.service;


import com.happiest.feedback.constant.PredefinedConstants;
import com.happiest.feedback.dao.FeedbackInterface;
import com.happiest.feedback.dto.Feedback;
import com.happiest.feedback.exceptions.PincodeNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService {
    private static final Logger LOGGER = LogManager.getLogger(FeedbackService.class);

    @Autowired
    private FeedbackInterface feedbackInterface;

    // Method to submit feedback with exception handling
    public Feedback submitFeedback(Feedback feedback) {
        LOGGER.info(PredefinedConstants.FEEDBACK_ADD_INFO1, feedback);
        try {
            Feedback savedFeedback = feedbackInterface.save(feedback);
            LOGGER.info(PredefinedConstants.FEEDBACK_ADD_INFO2, savedFeedback.getFid());
            return savedFeedback;
        }
        catch (Exception e) {
            LOGGER.error(PredefinedConstants.FEEDBACK_ADD_ERROR, e.getMessage(), e);
            throw new RuntimeException(PredefinedConstants.FEEDBACK_ADD_ERROR, e);
        }
    }

    // Method to get all feedback with exception handling
    public List<Feedback> getAllFeedbacks() {
        LOGGER.info(PredefinedConstants.FEEDBACK_FETCH_INFO1);
        try {
            List<Feedback> feedbackList = feedbackInterface.findAll();
            LOGGER.info(PredefinedConstants.FEEDBACK_FETCH_INFO2, feedbackList.size());
            return feedbackList;
        } catch (Exception e) {
            LOGGER.error(PredefinedConstants.FEEDBACK_FETCH_ERROR, e.getMessage(), e);
            throw new RuntimeException(PredefinedConstants.FEEDBACK_FETCH_ERROR, e);
        }
    }

    public long countFeedbacksByPincode(long pincode) {
        try {
            long count = feedbackInterface.countFeedbackByPincode(pincode);
            if (count == 0) {
                throw new PincodeNotFoundException("No feedback found for pincode: " + pincode);
            }
            return count;
        } catch (Exception e) {
            // Log the exception if needed
            throw new RuntimeException("Error occurred while counting feedbacks: " + e.getMessage(), e);
        }
    }
}