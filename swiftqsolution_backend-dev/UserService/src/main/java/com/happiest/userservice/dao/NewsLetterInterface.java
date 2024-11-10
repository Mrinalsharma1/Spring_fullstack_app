package com.happiest.userservice.dao;

import com.happiest.userservice.dto.NewsLetter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NewsLetterInterface extends JpaRepository<NewsLetter, Long> {
    @Query("SELECT n.email FROM NewsLetter n")
    List<String> findAllSubscriberEmails();
}
