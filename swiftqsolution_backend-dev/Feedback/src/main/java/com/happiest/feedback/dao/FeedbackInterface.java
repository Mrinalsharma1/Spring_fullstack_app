package com.happiest.feedback.dao;


import com.happiest.feedback.dto.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FeedbackInterface extends JpaRepository<Feedback, Long> {

    @Query("SELECT COUNT(f) FROM Feedback f WHERE f.pincode = :pincode")
    long countFeedbackByPincode(@Param("pincode") long pincode);
}
