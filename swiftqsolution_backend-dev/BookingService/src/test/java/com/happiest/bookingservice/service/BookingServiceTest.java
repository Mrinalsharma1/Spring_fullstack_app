package com.happiest.bookingservice.service;

import com.happiest.bookingservice.dao.BookingInterface;
import com.happiest.bookingservice.dao.SlotInterface;
import com.happiest.bookingservice.dao.UserInterface;
import com.happiest.bookingservice.dto.*;
import com.happiest.bookingservice.exception.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.lang.IllegalStateException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
class BookingServiceTest {

    @Mock
    private BookingInterface bookingInterface;
    @Mock
    private SlotInterface slotInterface;
    @Mock
    private UserInterface userInterface;

    @InjectMocks
    private BookingService bookingService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // Test cases for createBooking
    @Test
    void testCreateBookingSuccess() {
        Booking booking = new Booking();
        Slot slot = new Slot(); slot.setSlotid(1L);
        UserEntity user = new UserEntity(); user.setId(1L);

        booking.setSlot(slot);
        booking.setUser(user);

        when(slotInterface.findById(1L)).thenReturn(Optional.of(slot));
        when(userInterface.findById(1L)).thenReturn(Optional.of(user));
        when(bookingInterface.save(booking)).thenReturn(booking);

        Booking result = bookingService.createBooking(booking, "testUser");

        assertNotNull(result);
        verify(bookingInterface).save(booking);
    }

    @Test
    public void testGetAllBookings_Exception() {
        when(bookingInterface.findAllBookings()).thenThrow(new RuntimeException("Database error"));

        List<BookingResponse> bookingResponses = bookingService.getAllBookings();

        assertTrue(bookingResponses.isEmpty());
        // Optionally, you can verify that the error message was logged
        // This requires a logging framework and additional setup
    }
    @Test
    public void testCancelBooking_Exception() {
        Long bookingId = 1L;
        when(bookingInterface.findById(bookingId)).thenReturn(Optional.of(new Booking()));
        doThrow(new RuntimeException("Database error")).when(bookingInterface).deleteById(bookingId);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            bookingService.cancelBooking(bookingId);
        });

        assertEquals("Error while deleting booking: Database error", exception.getMessage());
    }

    @Test
    void testCreateBookingSlotNotFound() {
        Booking booking = new Booking();
        Slot slot = new Slot();
        slot.setSlotid(1L);  // Set the slot ID manually
        booking.setSlot(slot);

        when(slotInterface.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> bookingService.createBooking(booking, "testUser"));
    }

    @Test
    void testCreateBookingUserNotFound() {
        Booking booking = new Booking();
        UserEntity user = new UserEntity();
        user.setId(1L);  // Set the user ID manually
        booking.setUser(user);

        when(slotInterface.findById(anyLong())).thenReturn(Optional.of(new Slot()));
        when(userInterface.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> bookingService.createBooking(booking, "testUser"));
    }

    @Test
    public void testGetBookingsBetweenDates_Exception() {
        String startDate = "2024-01-01";
        String endDate = "2024-01-31";
        when(bookingInterface.findBookingsBetweenDates(startDate, endDate)).thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            bookingService.getBookingsBetweenDates(startDate, endDate);
        });

        assertEquals("Error while fetching bookings: Database error", exception.getMessage());
    }

    @Test
    public void testGetTimingsByPincodeAndDate_Exception() {
        long pincode = 123456;
        String date = "2024-01-01";
        when(bookingInterface.findTimingsByPincodeAndDate(pincode, date)).thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            bookingService.getTimingsByPincodeAndDate(pincode, date);
        });

        assertEquals("Error while fetching timings: Database error", exception.getMessage());
    }



    // Test cases for getBookingCountForPincode
    @Test
    void testGetBookingCountForPincodeSuccess() {
        when(bookingInterface.countBookingsByPincode(12345L)).thenReturn(10L);

        long result = bookingService.getBookingCountForPincode(12345L);

        assertEquals(10L, result);
    }

    @Test
    void testGetBookingCountForPincodeException() {
        when(bookingInterface.countBookingsByPincode(anyLong())).thenThrow(new InvalidPincodeException("Invalid pincode"));

        assertThrows(InvalidPincodeException.class, () -> bookingService.getBookingCountForPincode(12345L));
    }

    // Test cases for getBookingsBetweenDates
    @Test
    void testGetBookingsBetweenDatesSuccess() {
        List<Booking> bookings = new ArrayList<>();
        when(bookingInterface.findBookingsBetweenDates("2023-01-01", "2023-12-31")).thenReturn(bookings);

        List<Booking> result = bookingService.getBookingsBetweenDates("2023-01-01", "2023-12-31");

        assertEquals(bookings, result);
    }

    @Test
    void testGetBookingsBetweenDatesNoBookingsFound() {
        when(bookingInterface.findBookingsBetweenDates(anyString(), anyString())).thenThrow(new NoBookingsFoundException("No bookings found"));

        assertThrows(NoBookingsFoundException.class, () -> bookingService.getBookingsBetweenDates("2023-01-01", "2023-12-31"));
    }

    // Test cases for getTimingsByPincodeAndDate
    @Test
    void testGetTimingsByPincodeAndDateSuccess() {
        List<Object[]> results = new ArrayList<>();
        results.add(new Object[]{"10:00 AM", "available"});

        when(bookingInterface.findTimingsByPincodeAndDate(12345L, "2023-11-01")).thenReturn(results);

        List<TimingResponse1> timings = bookingService.getTimingsByPincodeAndDate(12345L, "2023-11-01");

        assertFalse(timings.isEmpty());
        assertEquals("10:00 AM", timings.get(0).getTiming());
    }

    // Test cases for getAllBookings
    @Test
    void testGetAllBookings() {
        List<BookingResponse> bookings = new ArrayList<>();
        when(bookingInterface.findAllBookings()).thenReturn(bookings);

        List<BookingResponse> result = bookingService.getAllBookings();

        assertEquals(bookings, result);
    }

    // Test cases for getAllBookingDetails
    @Test
    void testGetAllBookingDetailsSuccess() {
        List<BookingResponse2> details = new ArrayList<>();
        when(bookingInterface.findAllBookingDetails()).thenReturn(details);

        List<BookingResponse2> result = bookingService.getAllBookingDetails();

        assertEquals(details, result);
    }

    // Test cases for updateBookingStatusToCompleted
    @Test
    void testUpdateBookingStatusToCompletedSuccess() {
        Booking booking = new Booking();
        booking.setStatus("confirmed");
        when(bookingInterface.findById(anyLong())).thenReturn(Optional.of(booking));
        when(bookingInterface.save(booking)).thenReturn(booking);

        Booking result = bookingService.updateBookingStatusToCompleted(1L, "testUser");

        assertEquals("completed", result.getStatus());
    }

    @Test
    void testUpdateBookingStatusBookingNotFound() {
        when(bookingInterface.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NoBookingsFoundException.class, () -> bookingService.updateBookingStatusToCompleted(1L, "testUser"));
    }

//    @Test
//    void testUpdateBookingStatusInvalidStatus() {
//        Booking booking = new Booking();
//        booking.setStatus("pending");
//        when(bookingInterface.findById(anyLong())).thenReturn(Optional.of(booking));
//
//        assertThrows(IllegalStateException.class, () -> bookingService.updateBookingStatusToCompleted(1L, "testUser"));
//    }

    // Test cases for cancelBooking
    @Test
    void testCancelBookingSuccess() {
        Booking booking = new Booking();
        when(bookingInterface.findById(1L)).thenReturn(Optional.of(booking));

        bookingService.cancelBooking(1L);

        verify(bookingInterface, times(1)).deleteById(1L);
    }

    @Test
    void testCancelBookingNotFound() {
        when(bookingInterface.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoBookingsFoundException.class, () -> bookingService.cancelBooking(1L));
    }

    @Test
    public void testGetBookingCountForPincode_Exception() {
        long pincode = 123456;
        when(bookingInterface.countBookingsByPincode(pincode)).thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            bookingService.getBookingCountForPincode(pincode);
        });

        assertEquals("Error while fetching booking count: Database error", exception.getMessage());
    }
}
