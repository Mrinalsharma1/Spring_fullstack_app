package com.happiest.userservice.dao;

import com.happiest.userservice.dto.ContactUs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ContactUsInterface extends JpaRepository<ContactUs, Long> {
    List<ContactUs> findByStatusFalse();


    Optional<ContactUs> findByEmail(String email);

    @Modifying
    @Query("UPDATE ContactUs c SET c.status = true WHERE c.email = :email")
    void updateStatusByEmail(@Param("email") String email);
}
