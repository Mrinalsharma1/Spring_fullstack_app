package com.happiest.apigateway.apigateway.bookingservice.controller;

import com.happiest.apigateway.apigateway.bookingservice.model.*;
import com.happiest.apigateway.apigateway.bookingservice.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class BookingServiceController {

    @Autowired
    private BookingInterface bookingInterface;

    @Autowired
    private SlotInterface slotInterface;

    @Autowired
    private ProductInterface productInterface;

    @Autowired
    private ServiceTypeInterface serviceTypeInterface;





    //product endpoints starts here

    @PostMapping("/addproduct")

    public ResponseEntity<?> addProduct(@RequestBody Product product){
        Map<String, Object> response = new HashMap<>();
        try {
            return productInterface.addProduct(product);
        }
        catch(Exception e){
            response.put("status", "fail");
            response.put("message", "" + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getallproducts")
    public ResponseEntity<?> getAllProducts(){
        Map<String, Object> response = new HashMap<>();
        try {
            return productInterface.getAllProducts();
        }
        catch(Exception e){
            response.put("status", "fail");
            response.put("message", "" + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

    }


    //servicetype endpoints starts here

    @PostMapping("/addservice")
    public ResponseEntity<?> addServiceType(@RequestBody ServiceType serviceType){
        Map<String, Object> response = new HashMap<>();
        try {
            return serviceTypeInterface.addServiceType(serviceType);
        }
        catch(Exception e){
            response.put("status", "fail");
            response.put("message", "" + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/getallservices")
    public ResponseEntity<?> getAllServiceTypes(){
        Map<String, Object> response = new HashMap<>();
        try {
            return serviceTypeInterface.getAllServiceTypes();
        }
        catch(Exception e){
            response.put("status", "fail");
            response.put("message", "" + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    //slot endpoints starts here

    @PostMapping("/addslot")
    public ResponseEntity<?> addSlot(@RequestBody Slot slot){
        return slotInterface.addSlot(slot);
    }

    @GetMapping("/availableslots")
    public ResponseEntity<Long> getAvailableSlots(
            @RequestParam("pincode") long pincode,
            @RequestParam("date") String date){
        return slotInterface.getAvailableSlots(pincode, date);
    }

    @GetMapping("/pincodetimings")
    public ResponseEntity<Map<String, Object>> getTimings(@RequestParam long pincode){
        Map<String, Object> response = new HashMap<>();
        try{
            return slotInterface.getTimings(pincode);
        }
        catch(Exception e){
            response.put("status", "fail");
            response.put("message", "" + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

    }
    @PutMapping("/updatetime/{slotId}")
    public ResponseEntity<Map<String, Object>> updateSlotTiming(
            @PathVariable Long slotId,
            @RequestBody UpdateSlotTimingRequest request){
        Map<String, Object> response = new HashMap<>();
        try{
            return slotInterface.updateSlotTiming(slotId,request );
        }
        catch(Exception e){
            response.put("status", "fail");
            response.put("message", "" + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

    }


    //booking endpoints start here

    @PostMapping("/bookslot/{Username}")
    public ResponseEntity<Map<String, Object>> addBooking(@RequestBody Booking booking, @PathVariable String Username){
        Map<String, Object> response = new HashMap<>();
        try{
            return bookingInterface.addBooking(booking,Username);
        }
        catch(Exception e){
            response.put("status", "fail");
            response.put("message", "" + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/countbookings/{pincode}")
    public ResponseEntity<Map<String, Object>> getBookingCount(@PathVariable long pincode){
        Map<String, Object> response = new HashMap<>();
        try {
            return bookingInterface.getBookingCount(pincode);
        }
        catch(Exception e){
            response.put("status", "fail");
            response.put("message", "" + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/between-dates")
    public ResponseEntity<Map<String, Object>> getBookingsBetweenDates(
            @RequestParam("startDate") String startDateStr,
            @RequestParam("endDate") String endDateStr){
        Map<String, Object> response = new HashMap<>();
        try {
            return bookingInterface.getBookingsBetweenDates(startDateStr, endDateStr);
        }
        catch(Exception e){
            response.put("status", "fail");
            response.put("message", "" + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/timings")
    public ResponseEntity<Map<String, Object>> getTimings(
            @RequestParam long pincode,
            @RequestParam String date){
        Map<String, Object> response = new HashMap<>();
        try {
            return bookingInterface.getTimings(pincode, date);
        }
        catch(Exception e){
            response.put("status", "fail");
            response.put("message", "" + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/bookings")
    public ResponseEntity<Map<String, Object>> getAllBookings(){
        Map<String, Object> response = new HashMap<>();
        try {
            return bookingInterface.getAllBookings();
        }
        catch(Exception e){
            response.put("status", "fail");
            response.put("message", "" + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/user-booking-details")  // Define your endpoint
    public ResponseEntity<Map<String, Object>> getBookingDetails(){

            Map<String, Object> response = new HashMap<>();
            try {
                return bookingInterface.getBookingDetails();
            }
            catch(Exception e){
                response.put("status", "fail");
                response.put("message", "" + e.getMessage());
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

        }

    @PutMapping("/update-status/{bookingId}/{Username}")
    public ResponseEntity<Map<String, Object>> completeBooking(@PathVariable Long bookingId, @PathVariable String Username){
        Map<String, Object> response = new HashMap<>();
        try {
            return bookingInterface.completeBooking(bookingId, Username);
        }
        catch(Exception e){
            response.put("status", "fail");
            response.put("message", "" + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

    }
    @DeleteMapping("/delete-booking/{bookingId}")
    public ResponseEntity<Map<String, Object>> deleteBooking(@PathVariable Long bookingId){
        Map<String, Object> response = new HashMap<>();
        try {
            return bookingInterface.cancelBooking(bookingId);
        }
        catch(Exception e){
            response.put("status", "fail");
            response.put("message", "" + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/completed/count")
    public ResponseEntity<Map<String, Object>> countCompletedBookings(@RequestParam Long pincode){
        Map<String, Object> response = new HashMap<>();
        try {
            return bookingInterface.countCompletedBookings(pincode);
        }
        catch(Exception e){
            response.put("status", "fail");
            response.put("message", "" + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/bookings/count/completed")
    public ResponseEntity<Map<String, Object>> countTotalCompletedBookings(){
        Map<String, Object> response = new HashMap<>();
        try {
            return bookingInterface.countTotalCompletedBookings();
        }
        catch(Exception e){
            response.put("status", "fail");
            response.put("message", "" + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

    }



    }





