package com.happiest.apigateway.apigateway.bookingservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "booking")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto-generated ID
    @Column(name = "bookingid")
    private long bookingid;

    @Column(name = "bdate",nullable=false)
    private String bdate;

    @Column(name = "status",nullable=false)
    private String status;

    @OneToOne
    @JoinColumn(name = "slotid", referencedColumnName = "slotid")
    Slot slot;

    @ManyToOne
    @JoinColumn(name = "id", referencedColumnName = "id")
    UserEntity user;


    public Booking(long l, String testuser, long l1, String date, String s, String pending) {
    }

    public void setUsername(String testuser) {
    }

    public void setPincode(long l) {
    }

    public void setDate(String date) {
    }

    public void setStartTime(String s) {

    }
}
