package com.happiest.userservice.dto;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name="users")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto-generated ID
    @Column(name = "id")
    private long id;   // Primary key, auto-generated

    @Column(nullable = false)
    private String profilename;

    @Column(nullable = false, unique = true)  // Ensure email is unique
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;

    private String resetToken;
    private LocalDateTime resetTokenExpiry;
}


