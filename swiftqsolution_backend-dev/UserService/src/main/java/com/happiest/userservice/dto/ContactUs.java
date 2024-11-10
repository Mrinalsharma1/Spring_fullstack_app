package com.happiest.userservice.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "contactus")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContactUs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contactid; // Unique identifier for the contact message

    @Column(name = "name", nullable = false)
    private String name; // Name of the person contacting

    @Column(name = "email", nullable = false,unique = true)
    private String email; // Email address of the person contacting

    @Column(name = "phone", nullable = false)
    private String phone; // Phone number of the person contacting

    @Column(name = "subject", nullable = false)
    private String subject; // Subject of the message

    @Column(name = "message", nullable = false)
    private String message; // The message content

    @Column(name = "status", nullable = false, columnDefinition = "boolean default false")
    private boolean status = false; // The message status, default value set to true

}