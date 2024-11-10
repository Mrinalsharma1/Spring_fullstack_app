package com.happiest.apigateway.apigateway.bookingservice.repository;


import com.happiest.apigateway.apigateway.bookingservice.model.Booking;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name="http://BookingService/booking")
public interface BookingInterface{



    @PostMapping("/bookslot/{Username}")
    public ResponseEntity<Map<String, Object>> addBooking(@RequestBody Booking booking, @PathVariable String Username);

    @GetMapping("/countbookings/{pincode}")
    public ResponseEntity<Map<String, Object>> getBookingCount(@PathVariable long pincode);

    @GetMapping("/between-dates")
    public ResponseEntity<Map<String, Object>> getBookingsBetweenDates(
            @RequestParam("startDate") String startDateStr,
            @RequestParam("endDate") String endDateStr);


    @GetMapping("/timings")
    public ResponseEntity<Map<String, Object>> getTimings(
            @RequestParam long pincode,
            @RequestParam String date);

    @GetMapping("/bookings")
    public ResponseEntity<Map<String, Object>> getAllBookings();

    @GetMapping("/user-booking-details")  // Define your endpoint
    public ResponseEntity<Map<String, Object>> getBookingDetails();

    @PutMapping("/update-status/{bookingId}/{Username}")
    public ResponseEntity<Map<String, Object>> completeBooking(@PathVariable Long bookingId, @PathVariable String Username);

    @DeleteMapping("/delete-booking/{bookingId}")
    public ResponseEntity<Map<String, Object>> cancelBooking(@PathVariable Long bookingId);

    @GetMapping("/completed/count")
    public ResponseEntity<Map<String, Object>> countCompletedBookings(@RequestParam Long pincode);

    @GetMapping("/bookings/count/completed")
    public ResponseEntity<Map<String, Object>> countTotalCompletedBookings();


}
