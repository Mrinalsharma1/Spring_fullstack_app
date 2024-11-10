package com.happiest.userservice.dao;

import com.happiest.userservice.dto.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackInterface extends JpaRepository<Feedback, Long> {
}
