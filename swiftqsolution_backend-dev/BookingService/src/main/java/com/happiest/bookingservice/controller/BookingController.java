package com.happiest.bookingservice.controller;

import com.happiest.bookingservice.dto.*;

import com.happiest.bookingservice.exception.*;
import com.happiest.bookingservice.service.BookingService;
import com.happiest.bookingservice.service.EmailService;
import com.happiest.bookingservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/booking")
@CrossOrigin
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    @PostMapping("/bookslot/{Username}")
    public ResponseEntity<Map<String, Object>> addBooking(@RequestBody Booking booking, @PathVariable String Username) {
        Map<String, Object> responseBody = new HashMap<>();
        try {
            Booking createdBooking = bookingService.createBooking(booking,Username);

            // Create a password reset URL
            String resetUrl = "http://localhost:3000/bookings";

            // Send the reset link via email by
            emailService.sendEmail(Username, "Your slot is booked successfully",
                    "Click the link to view your booking details: " + resetUrl);

            responseBody.put("status", "success");
            responseBody.put("message", "Slot Booked successfully");
            responseBody.put("booking", createdBooking);
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } catch (RuntimeException e) {
            // Log the error message (consider using a logging framework)
            responseBody.put("status", "fail");
            responseBody.put("message", "Error occurred: " + e.getMessage());
            return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/countbookings/{pincode}")
    public ResponseEntity<Map<String, Object>> getBookingCount(@PathVariable long pincode) {
        Map<String, Object> responseBody = new HashMap<>();
        try {
            long count = bookingService.getBookingCountForPincode(pincode);

            // Prepare the success response
            responseBody.put("status", "success");
            responseBody.put("message", "Booking count retrieved successfully");
            responseBody.put("count", count);
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } catch (PincodeNotFoundException e) {
            // Handle the specific case when the pincode is not found
            responseBody.put("status", "fail");
            responseBody.put("message", "Error occured: " + e.getMessage());
            return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND); // Use NOT_FOUND (404) status
        } catch (DataAccessException e) {
            responseBody.put("status", "fail");
            responseBody.put("message", "Database error occurred: " + e.getMessage());
            return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR); // Use INTERNAL_SERVER_ERROR (500) status
        }  catch (Exception e) {
            // Log the error (consider using a logging framework)
            System.err.println("Error while fetching booking count: " + e.getMessage());
            responseBody.put("status", "fail");
            responseBody.put("message", "An unexpected error occurred: " + e.getMessage());
            return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR); // Use INTERNAL_SERVER_ERROR (500) status
        }
    }




    @GetMapping("/between-dates")
    public ResponseEntity<Map<String, Object>> getBookingsBetweenDates(
            @RequestParam("startDate") String startDateStr,
            @RequestParam("endDate") String endDateStr) {
        Map<String, Object> responseBody = new HashMap<>();
        try {
            // Fetch bookings between the specified date strings
            List<Booking> bookings = bookingService.getBookingsBetweenDates(startDateStr, endDateStr);

            // Prepare the success response
            responseBody.put("status", "success");
            responseBody.put("message", "Bookings retrieved successfully");
            responseBody.put("bookings", bookings);
            return new ResponseEntity<>(responseBody, HttpStatus.OK);

           // Use BAD_REQUEST (400) status
        } catch (NoBookingsFoundException e) {
            // Handle the case when no bookings are found for the provided date range
            responseBody.put("status", "fail");
            responseBody.put("message", "Error occured: " + e.getMessage());
            return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND); // Use NOT_FOUND (404) status
        } catch (Exception e) {
            // Log the error (consider using a logging framework)
            System.err.println("Error while fetching bookings between dates: " + e.getMessage());
            responseBody.put("status", "fail");
            responseBody.put("message", "An unexpected error occurred: " + e.getMessage());
            return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR); // Use INTERNAL_SERVER_ERROR (500) status
        }
    }
    @GetMapping("/timings")
    public ResponseEntity<Map<String, Object>> getTimings(
            @RequestParam long pincode,
            @RequestParam String date) {
        Map<String, Object> responseBody = new HashMap<>();
        try {
            List<TimingResponse1> timings = bookingService.getTimingsByPincodeAndDate(pincode, date);

            // Prepare the success response
            responseBody.put("status", "success");
            responseBody.put("message", "Timings retrieved successfully");
            responseBody.put("timings", timings);
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } catch (NoTimingsFoundException e) {
            // Handle the case when no timings are found for the given pincode and date
            responseBody.put("status", "fail");
            responseBody.put("message", "Error occured: " + e.getMessage());
            return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND); // Use NOT_FOUND (404) status
        } catch (InvalidPincodeException e) {
            // Handle the case where the pincode is invalid
            responseBody.put("status", "fail");
            responseBody.put("message", "Error occured: " + e.getMessage());
            return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST); // Use BAD_REQUEST (400) status
        } catch (Exception e) {
            // Log the error (consider using a logging framework)
            System.err.println("Error while fetching timings: " + e.getMessage());
            responseBody.put("status", "fail");
            responseBody.put("message", "An unexpected error occurred: " + e.getMessage());
            return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR); // Use INTERNAL_SERVER_ERROR (500) status
        }
    }

    @GetMapping("/bookings")
    public ResponseEntity<Map<String, Object>> getAllBookings() {
        Map<String, Object> responseBody = new HashMap<>();
        try {
            List<BookingResponse> bookings = bookingService.getAllBookings();

            // Prepare the success response
            responseBody.put("status", "success");
            responseBody.put("message", "Bookings retrieved successfully");
            responseBody.put("data", bookings);

            return ResponseEntity.ok(responseBody); // HTTP 200 OK
        } catch (Exception e) {
            // Log the error (consider using a logging framework)
            System.err.println("Error fetching bookings: " + e.getMessage());

            // Prepare the error response
            responseBody.put("status", "fail");
            responseBody.put("message", "An error occurred while fetching bookings: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody); // HTTP 500 Internal Server Error
        }
    }

    @GetMapping("/user-booking-details")  // Define your endpoint
    public ResponseEntity<Map<String, Object>> getBookingDetails() {
        Map<String, Object> responseBody = new HashMap<>();
        try {
            List<BookingResponse2> bookingDetails = bookingService.getAllBookingDetails();

            // Prepare the response body
            responseBody.put("status", "success");
            responseBody.put("message", "Booking details retrieved successfully.");
            responseBody.put("data", bookingDetails);

            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } catch (Exception e) {
            // Log the error (consider using a logging framework)
            System.err.println("Error while fetching booking details: " + e.getMessage());

            // Prepare an error response
            responseBody.put("status", "fail");
            responseBody.put("message", "Error occurred: " + e.getMessage());
            return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update-status/{bookingId}/{Username}")
    public ResponseEntity<Map<String, Object>> completeBooking(@PathVariable Long bookingId, @PathVariable String Username) {
        Map<String, Object> response = new HashMap<>();
        try {
            Booking updatedBooking = bookingService.updateBookingStatusToCompleted(bookingId,Username);


            // Create a password reset URL
            String resetUrl = "http://localhost:3000/userfeedback";

            // Send the reset link via email by
            emailService.sendEmail(Username, "Your service is completed",
                    "Click the link to share the feedback: " + resetUrl);


            response.put("message", "Booking status updated to 'completed' successfully");
            response.put("status", "success");
            response.put("bookingId", updatedBooking.getBookingid());
            response.put("newStatus", updatedBooking.getStatus());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (NoBookingsFoundException e) {
            response.put("Error occured: ", e.getMessage());
            response.put("status", "fail");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            response.put("message", "Error occurred: " + e.getMessage());
            response.put("status", "fail");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete-booking/{bookingId}")
    public ResponseEntity<Map<String, Object>> cancelBooking(@PathVariable Long bookingId) {
        Map<String, Object> responseBody = new HashMap<>();

        try {
            bookingService.cancelBooking(bookingId);
            responseBody.put("message", "Booking deleted successfully");
            responseBody.put("status", "success");
            return new ResponseEntity<>(responseBody, HttpStatus.OK); // 204 No Content

        } catch (NoBookingsFoundException e) {
            responseBody.put("message", "Error occurred: " + e.getMessage());
            responseBody.put("status", "fail");
            return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            responseBody.put("message", "Error occurred: " + e.getMessage());
            responseBody.put("status", "fail");
            return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/completed/count")
    public ResponseEntity<Map<String, Object>> countCompletedBookings(@RequestParam Long pincode) {
        Map<String, Object> responseBody = new HashMap<>();

        try {
            long count = bookingService.countCompletedBookingsByPincode(pincode);
            responseBody.put("count", count);
            responseBody.put("message", "Count retrieved successfully");
            responseBody.put("status", "success");
            return ResponseEntity.ok(responseBody); // 200 OK

        } catch (PincodeNotFoundException e) {
            responseBody.put("message", "Error occurred: " + e.getMessage());
            responseBody.put("status", "fail");
            return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND); // 404 Not Found

        } catch (RuntimeException e) {
            responseBody.put("message", "Error occurred: " + e.getMessage());
            responseBody.put("status", "fail");
            return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR); // 500 Internal Server Error
        }
    }

    @GetMapping("/bookings/count/completed")
    public ResponseEntity<Map<String, Object>> countTotalCompletedBookings() {
        Map<String, Object> responseBody = new HashMap<>();

        try {
            long count = bookingService.countTotalCompletedBookings();
            responseBody.put("count", count);
            responseBody.put("message", "Completed bookings count retrieved successfully");
            responseBody.put("status", "success");
            return ResponseEntity.ok(responseBody);
        } catch (NoBookingsFoundException e) {
            responseBody.put("message", "No completed bookings found: " + e.getMessage());
            responseBody.put("status", "fail");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
        } catch (RuntimeException e) {
            responseBody.put("message", "An error occurred: " + e.getMessage());
            responseBody.put("status", "fail");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
        }
    }


}
