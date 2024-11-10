package com.happiest.apigateway.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Users {
    @Id
    private long id;   // Primary key, auto-generated

    @Column(nullable = false)
    private String profilename;

    @Column(nullable = false, unique = true)  // Ensure email is unique
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;
}

