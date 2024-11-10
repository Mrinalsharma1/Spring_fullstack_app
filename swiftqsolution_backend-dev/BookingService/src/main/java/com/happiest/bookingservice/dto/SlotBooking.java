package com.happiest.bookingservice.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "slot_booking")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SlotBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer sbId;

    @Column(name = "date", nullable = false)
    private String date;

    @ManyToOne
    @JoinColumn(name = "slot_id")
    private Slot slot;

    @Column(name = "checked", nullable = false)
    private Boolean checked;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;


}
