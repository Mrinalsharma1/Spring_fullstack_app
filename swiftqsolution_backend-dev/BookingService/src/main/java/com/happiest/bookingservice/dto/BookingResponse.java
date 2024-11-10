package com.happiest.bookingservice.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {
    private Long bookingid;
    private String bdate;
    private String status;
    private String timing;
    private Long pincode;
    private String username;
}
