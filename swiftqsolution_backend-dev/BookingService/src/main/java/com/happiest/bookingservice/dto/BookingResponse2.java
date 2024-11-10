package com.happiest.bookingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse2 {
    private long bookingId;            // Booking ID
    private String bdate;              // Booking date
    private long userId;
    private String status;
    private String timing;
    private String name;
    private String address;

}
