package com.happiest.apigateway.apigateway.bookingservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "slot")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Slot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto-generated ID
    @Column(name = "slotid")
    private long slotid;

    @Column(name = "timing",nullable=false)
    private String timing;

   @ManyToOne
   @JoinColumn(name = "pincode", referencedColumnName = "pincode")
    ServiceCenterEntity serviceCenter;


    public Slot(long l, String date, String s, String s1, long l1, int i) {
    }
}
