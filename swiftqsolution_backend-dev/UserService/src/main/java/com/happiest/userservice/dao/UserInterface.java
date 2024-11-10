package com.happiest.userservice.dao;

import com.happiest.userservice.dto.UserEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;


public interface UserInterface extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByResetToken(String resetToken);
    long countByRole(String role);
}

