package com.happiest.bookingservice.dto;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name="servicecenter")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceCenterEntity {
    @Id
    private long pincode;  // Assuming this is your primary key

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String address;

    @Column(nullable = false)
    private String open_from;

    @Column(nullable = false)
    private String open_to;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false)
    private String service_type;

    @Column(nullable = false)
    private String location;

    @Column(nullable = true, unique = true)
    private Long managerId;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)  // Ensure createdAt is populated correctly
    private LocalDateTime createdAt;
}
