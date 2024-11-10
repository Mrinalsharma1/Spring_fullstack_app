package com.happiest.bookingservice.service;

import com.happiest.bookingservice.dao.BookingInterface;
import com.happiest.bookingservice.dao.SlotInterface;
import com.happiest.bookingservice.dao.UserInterface;
import com.happiest.bookingservice.dto.*;

import com.happiest.bookingservice.exception.*;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private BookingInterface bookingInterface;

    @Autowired
    private SlotInterface slotInterface;

    @Autowired
    private UserInterface userInterface;

    private static final Logger LOGGER = LogManager.getLogger(BookingService.class);

    @Transactional
    public Booking createBooking(Booking booking, String Username) {
        try {
            // Validate the slot and user entities before saving
            Slot slot = slotInterface.findById(booking.getSlot().getSlotid())
                    .orElseThrow(() -> new RuntimeException("Slot not found"));
            UserEntity user = userInterface.findById(booking.getUser().getId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            booking.setSlot(slot);
            booking.setUser(user);
            return bookingInterface.save(booking);
        } catch (RuntimeException e) {
            // Handle specific exceptions as necessary
            throw new RuntimeException("Error while creating booking: " + e.getMessage());
        }
    }


    public long getBookingCountForPincode(long pincode) {
        long count = 0;
        try {
            count = bookingInterface.countBookingsByPincode(pincode);
        } catch(InvalidPincodeException e){
            throw e;

        }
        catch (Exception e) {
            // Log the error (consider using a logging framework)
            System.err.println("Error while fetching booking count: " + e.getMessage());
            // Optionally, rethrow or handle the exception as needed
            throw new RuntimeException("Error while fetching booking count: " + e.getMessage(), e);
        }
        return count;
    }

    public List<Booking> getBookingsBetweenDates(String startDate, String endDate) {

            try {
                // Call the repository method with String parameters
                return bookingInterface.findBookingsBetweenDates(startDate, endDate);
            }
            catch(NoBookingsFoundException e){
                throw e;
            }
            catch (Exception e) {
                // Log the error (consider using a logging framework)
                System.err.println("Error while fetching bookings between dates: " + e.getMessage());
                throw new RuntimeException("Error while fetching bookings: " + e.getMessage(), e);
            }
        }

    public List<TimingResponse1> getTimingsByPincodeAndDate(long pincode, String date) {
        try {
            List<Object[]> results = bookingInterface.findTimingsByPincodeAndDate(pincode, date);
            List<TimingResponse1> timings = new ArrayList<>();
            for (Object[] result : results) {
                String timing = (String) result[0];
                String status = (String) result[1];
                timings.add(new TimingResponse1(timing, status)); // Assuming you create a DTO for the response
            }
            return timings;
        } catch (Exception e) {
            // Log the error (consider using a logging framework)
            System.err.println("Error while fetching timings: " + e.getMessage());
            throw new RuntimeException("Error while fetching timings: " + e.getMessage(), e);
        }
    }

    public List<BookingResponse> getAllBookings() {
        List<BookingResponse> bookingResponses = new ArrayList<>();
        try {
            bookingResponses = bookingInterface.findAllBookings();
        } catch (Exception e) {
            // Log the error (consider using a logging framework)
            System.err.println("Error fetching bookings: " + e.getMessage());
            // You can choose to rethrow the exception or return an empty list or handle it based on your application logic
        }
        return bookingResponses;
    }

    public List<BookingResponse2> getAllBookingDetails() {
        try {
            return bookingInterface.findAllBookingDetails();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while retrieving booking details", e);
        }
    }



    @Transactional
    public Booking updateBookingStatusToCompleted(Long bookingId, String Username) {
        try {

            // Find the booking by ID
            Optional<Booking> bookingOpt = bookingInterface.findById(bookingId);
            if (bookingOpt.isEmpty()) {
                LOGGER.error("Booking with ID " + bookingId + " not found.");
                throw new NoBookingsFoundException("Booking not found with ID: " + bookingId);
            }

            // Update status to 'completed' if currently 'confirmed'
            Booking booking = bookingOpt.get();
            if (!"confirmed".equalsIgnoreCase(booking.getStatus())) {
                throw new IllegalStateException("Booking status must be 'confirmed' to update to 'completed'");
            }

            booking.setStatus("completed");
            Booking updatedBooking = bookingInterface.save(booking);

            LOGGER.info("Booking status for ID " + bookingId + " updated successfully to 'completed'");

            return updatedBooking;

        } catch (NoBookingsFoundException e) {
            LOGGER.error("Booking not found: " + e.getMessage(), e);
            throw e;  // Re-throw custom exception
        } catch (Exception e) {
            LOGGER.error("Error while updating booking status: " + e.getMessage(), e);
            throw new RuntimeException("Error while updating booking status: " + e.getMessage(), e);
        }
    }

    public void cancelBooking(Long bookingId) {
        try {
            LOGGER.info("Attempting to delete booking with ID: " + bookingId);

            // Check if the booking exists
            Optional<Booking> existingBooking = bookingInterface.findById(bookingId);
            if (existingBooking.isEmpty()) {
                LOGGER.error("Booking with ID " + bookingId + " does not exist.");
                throw new NoBookingsFoundException("Booking not found with ID: " + bookingId);
            }

            // Delete the booking
            bookingInterface.deleteById(bookingId);
            LOGGER.info("Booking with ID " + bookingId + " deleted successfully.");

        } catch (NoBookingsFoundException e) {
            throw e; // Re-throw custom exception
        } catch (Exception e) {
            LOGGER.error("Error while deleting booking: " + e.getMessage(), e);
            throw new RuntimeException("Error while deleting booking: " + e.getMessage(), e);
        }
    }

    public long countCompletedBookingsByPincode(Long pincode) {
        try {
            long count = bookingInterface.countCompletedBookingsByPincode(pincode);
            if (count == 0) {
                throw new PincodeNotFoundException("No completed bookings found for pincode: " + pincode);
            }
            return count;
        } catch (Exception e) {
            // Here you can log the error if needed
            throw new RuntimeException("Error while counting completed bookings: " + e.getMessage(), e);
        }
    }

    public long countTotalCompletedBookings() {
        try {
            long count = bookingInterface.countTotalCompletedBookings();
            if (count == 0) {
                throw new NoBookingsFoundException("No completed bookings found.");
            }
            return count;
        } catch (Exception e) {
            // Log the error if needed
            throw new RuntimeException("Error while counting completed bookings: " + e.getMessage(), e);
        }
    }


}
