package com.happiest.bookingservice.dao;

import com.happiest.bookingservice.dto.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInterface extends JpaRepository<UserEntity, Long> {

}
