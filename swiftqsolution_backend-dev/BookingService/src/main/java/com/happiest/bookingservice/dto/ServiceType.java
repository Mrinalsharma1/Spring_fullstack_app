package com.happiest.bookingservice.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "servicetype")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto-generated ID
    @Column(name = "serviceid")
    private long serviceid;

    @Column(name = "servicename",nullable=false, unique = true)
    private String servicename;

    @Column(name = "description")
    private String description;


}
