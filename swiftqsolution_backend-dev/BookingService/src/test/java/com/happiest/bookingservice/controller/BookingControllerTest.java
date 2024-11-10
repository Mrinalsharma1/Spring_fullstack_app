package com.happiest.bookingservice.controller;

import com.happiest.bookingservice.dao.BookingInterface;
import com.happiest.bookingservice.dto.*;
import com.happiest.bookingservice.exception.*;
import com.happiest.bookingservice.service.BookingService;
import com.happiest.bookingservice.service.EmailService;
import com.happiest.bookingservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
class BookingControllerTest {

    @Mock
    private BookingService bookingService;

    @Mock
    private BookingInterface bookingInterface;

    @Mock
    private EmailService emailService;

    @Mock
    private UserService userService;

    @InjectMocks
    private BookingController bookingController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddBookingSuccess() {
        Booking booking = new Booking();
        String username = "testuser";

        when(bookingService.createBooking(booking, username)).thenReturn(booking);
        doNothing().when(emailService).sendEmail(anyString(), anyString(), anyString());

        ResponseEntity<Map<String, Object>> response = bookingController.addBooking(booking, username);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("success", response.getBody().get("status"));
        assertEquals("Slot Booked successfully", response.getBody().get("message"));
    }

    @Test
    void testAddBookingRuntimeException() {
        Booking booking = new Booking();
        String username = "testuser";

        when(bookingService.createBooking(booking, username)).thenThrow(new RuntimeException("Booking error"));

        ResponseEntity<Map<String, Object>> response = bookingController.addBooking(booking, username);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("fail", response.getBody().get("status"));
        assertEquals("Error occurred: Booking error", response.getBody().get("message"));
    }

    @Test
    void testGetBookingCountSuccess() {
        long pincode = 12345L;
        long count = 10L;

        when(bookingService.getBookingCountForPincode(pincode)).thenReturn(count);

        ResponseEntity<Map<String, Object>> response = bookingController.getBookingCount(pincode);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("success", response.getBody().get("status"));
        assertEquals(count, response.getBody().get("count"));
    }

    @Test
    void testGetBookingCountPincodeNotFoundException() {
        long pincode = 12345L;

        when(bookingService.getBookingCountForPincode(pincode)).thenThrow(new PincodeNotFoundException("Pincode not found"));

        ResponseEntity<Map<String, Object>> response = bookingController.getBookingCount(pincode);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("fail", response.getBody().get("status"));
        assertEquals("Error occured: Pincode not found", response.getBody().get("message"));
    }

    @Test
    void testGetTimings_NoTimingsFoundException() {
        long pincode = 123456;
        String date = "2024-10-28";
        when(bookingService.getTimingsByPincodeAndDate(pincode, date))
                .thenThrow(new NoTimingsFoundException("No timings available"));

        ResponseEntity<Map<String, Object>> response = bookingController.getTimings(pincode, date);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("fail", response.getBody().get("status"));
        assertEquals("Error occured: No timings available", response.getBody().get("message"));
    }

    @Test
    void testGetTimings_InvalidPincodeException() {
        long pincode = 123456;
        String date = "2024-10-28";
        when(bookingService.getTimingsByPincodeAndDate(pincode, date))
                .thenThrow(new InvalidPincodeException("Invalid pincode"));

        ResponseEntity<Map<String, Object>> response = bookingController.getTimings(pincode, date);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("fail", response.getBody().get("status"));
        assertEquals("Error occured: Invalid pincode", response.getBody().get("message"));
    }

    @Test
    void testGetTimings_UnhandledException() {
        long pincode = 123456;
        String date = "2024-10-28";
        when(bookingService.getTimingsByPincodeAndDate(pincode, date))
                .thenThrow(new RuntimeException("Unexpected error"));

        ResponseEntity<Map<String, Object>> response = bookingController.getTimings(pincode, date);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("fail", response.getBody().get("status"));
        assertEquals("An unexpected error occurred: Unexpected error", response.getBody().get("message"));
    }

    @Test
    void testGetTimings_Success() {
        long pincode = 123456;
        String date = "2024-10-28";
        List<TimingResponse1> timings = Collections.singletonList(new TimingResponse1(/* constructor args */));
        when(bookingService.getTimingsByPincodeAndDate(pincode, date)).thenReturn(timings);

        ResponseEntity<Map<String, Object>> response = bookingController.getTimings(pincode, date);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("success", response.getBody().get("status"));
        assertEquals("Timings retrieved successfully", response.getBody().get("message"));
        assertEquals(timings, response.getBody().get("timings"));
    }

    @Test
    void testGetBookingCountUnexpectedException() {
        long pincode = 123456; // Sample pincode

        // Mock the service to throw a generic Exception
        doThrow(new RuntimeException("Unexpected error occurred"))
                .when(bookingService).getBookingCountForPincode(pincode);

        // Call the controller method
        ResponseEntity<Map<String, Object>> response = bookingController.getBookingCount(pincode);

        // Assert that the response status is 500 INTERNAL_SERVER_ERROR
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        // Assert that the response body contains the correct error message
        assertEquals("fail", response.getBody().get("status"));
        assertEquals("An unexpected error occurred: Unexpected error occurred", response.getBody().get("message"));
    }

    @Test
    void testGetAllBookings_Success() {
        // Arrange
        List<BookingResponse> bookings = Arrays.asList(new BookingResponse(/* parameters */));
        when(bookingService.getAllBookings()).thenReturn(bookings);

        // Act
        ResponseEntity<Map<String, Object>> response = bookingController.getAllBookings();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("success", response.getBody().get("status"));
        assertEquals("Bookings retrieved successfully", response.getBody().get("message"));
        assertEquals(bookings, response.getBody().get("data"));
    }

    @Test
    void testGetAllBookings_Exception() {
        // Arrange
        when(bookingService.getAllBookings()).thenThrow(new RuntimeException("Database error"));

        // Act
        ResponseEntity<Map<String, Object>> response = bookingController.getAllBookings();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("fail", response.getBody().get("status"));
        assertEquals("An error occurred while fetching bookings: Database error", response.getBody().get("message"));
    }
    @Test
    void testGetBookingsBetweenDatesUnexpectedException() {
        String startDateStr = "2024-10-01"; // Sample start date
        String endDateStr = "2024-10-31"; // Sample end date

        // Mock the service to throw a generic Exception
        doThrow(new RuntimeException("Unexpected error occurred"))
                .when(bookingService).getBookingsBetweenDates(startDateStr, endDateStr);

        // Call the controller method
        ResponseEntity<Map<String, Object>> response = bookingController.getBookingsBetweenDates(startDateStr, endDateStr);

        // Assert that the response status is 500 INTERNAL_SERVER_ERROR
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        // Assert that the response body contains the correct error message
        assertEquals("fail", response.getBody().get("status"));
        assertEquals("An unexpected error occurred: Unexpected error occurred", response.getBody().get("message"));
    }


    @Test
    void testGetBookingsBetweenDatesSuccess() {
        String startDate = "2023-01-01";
        String endDate = "2023-01-31";
        List<Booking> bookings = new ArrayList<>();

        when(bookingService.getBookingsBetweenDates(startDate, endDate)).thenReturn(bookings);

        ResponseEntity<Map<String, Object>> response = bookingController.getBookingsBetweenDates(startDate, endDate);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("success", response.getBody().get("status"));
        assertEquals(bookings, response.getBody().get("bookings"));
    }

    @Test
    void testGetBookingsBetweenDatesNoBookingsFoundException() {
        String startDate = "2023-01-01";
        String endDate = "2023-01-31";

        when(bookingService.getBookingsBetweenDates(startDate, endDate)).thenThrow(new NoBookingsFoundException("No bookings found"));

        ResponseEntity<Map<String, Object>> response = bookingController.getBookingsBetweenDates(startDate, endDate);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("fail", response.getBody().get("status"));
        assertEquals("Error occured: No bookings found", response.getBody().get("message"));
    }



    @Test
    void testGetTimingsSuccess() {
        long pincode = 12345L;
        String date = "2023-01-01";
        List<TimingResponse1> timings = new ArrayList<>();

        when(bookingService.getTimingsByPincodeAndDate(pincode, date)).thenReturn(timings);

        ResponseEntity<Map<String, Object>> response = bookingController.getTimings(pincode, date);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("success", response.getBody().get("status"));
        assertEquals(timings, response.getBody().get("timings"));
    }

    @Test
    void testGetTimingsNoTimingsFoundException() {
        long pincode = 12345L;
        String date = "2023-01-01";

        when(bookingService.getTimingsByPincodeAndDate(pincode, date)).thenThrow(new NoTimingsFoundException("No timings found"));

        ResponseEntity<Map<String, Object>> response = bookingController.getTimings(pincode, date);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("fail", response.getBody().get("status"));
        assertEquals("Error occured: No timings found", response.getBody().get("message"));
    }

    @Test
    void testGetTimingsInvalidPincodeException() {
        long pincode = 12345L;
        String date = "2023-01-01";

        when(bookingService.getTimingsByPincodeAndDate(pincode, date)).thenThrow(new InvalidPincodeException("Invalid pincode"));

        ResponseEntity<Map<String, Object>> response = bookingController.getTimings(pincode, date);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("fail", response.getBody().get("status"));
        assertEquals("Error occured: Invalid pincode", response.getBody().get("message"));
    }

    @Test
    void testGetAllBookingsSuccess() {
        List<BookingResponse> bookings = new ArrayList<>();

        when(bookingService.getAllBookings()).thenReturn(bookings);

        ResponseEntity<Map<String, Object>> response = bookingController.getAllBookings();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("success", response.getBody().get("status"));
        assertEquals(bookings, response.getBody().get("data"));
    }

    @Test
    void testGetAllBookingsUnexpectedException() {
        when(bookingService.getAllBookings()).thenThrow(new RuntimeException("Unexpected error"));

        ResponseEntity<Map<String, Object>> response = bookingController.getAllBookings();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("fail", response.getBody().get("status"));
        assertEquals("An error occurred while fetching bookings: Unexpected error", response.getBody().get("message"));
    }

    @Test
    void testGetBookingDetailsSuccess() {
        List<BookingResponse2> bookingDetails = new ArrayList<>();

        when(bookingService.getAllBookingDetails()).thenReturn(bookingDetails);

        ResponseEntity<Map<String, Object>> response = bookingController.getBookingDetails();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("success", response.getBody().get("status"));
        assertEquals(bookingDetails, response.getBody().get("data"));
    }

    @Test
    void testGetBookingDetailsUnexpectedException() {
        when(bookingService.getAllBookingDetails()).thenThrow(new RuntimeException("An error occurred while fetching booking details: Unexpected error"));

        ResponseEntity<Map<String, Object>> response = bookingController.getBookingDetails();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("fail", response.getBody().get("status"));
        assertEquals("Error occurred: An error occurred while fetching booking details: Unexpected error", response.getBody().get("message"));
    }

    @Test
    void testGetBookingCountDataAccessException() {
        long pincode = 123456; // Sample pincode

        // Mock the service to throw a DataAccessException
        doThrow(new DataAccessException("Database is not reachable") {})
                .when(bookingService).getBookingCountForPincode(pincode);

        // Call the controller method
        ResponseEntity<Map<String, Object>> response = bookingController.getBookingCount(pincode);

        // Assert that the response status is 500 INTERNAL_SERVER_ERROR
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        // Assert that the response body contains the correct error message
        assertEquals("fail", response.getBody().get("status"));
        assertEquals("Database error occurred: Database is not reachable", response.getBody().get("message"));
    }



    @Test
    void testGetBookingDetails_Success() {
        // Arrange
        List<BookingResponse2> bookingDetails = Arrays.asList(new BookingResponse2(/* parameters */));
        when(bookingService.getAllBookingDetails()).thenReturn(bookingDetails);

        // Act
        ResponseEntity<Map<String, Object>> response = bookingController.getBookingDetails();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("success", response.getBody().get("status"));
        assertEquals("Booking details retrieved successfully.", response.getBody().get("message"));
        assertEquals(bookingDetails, response.getBody().get("data"));
    }

    @Test
    public void testCompleteBooking_NoBookingsFoundException() {
        // Arrange
        Long bookingId = 1L;
        String username = "testuser@example.com";
        when(bookingService.updateBookingStatusToCompleted(bookingId, username))
                .thenThrow(new NoBookingsFoundException("No booking found for the given ID"));

        // Act
        ResponseEntity<Map<String, Object>> response = bookingController.completeBooking(bookingId, username);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("fail", response.getBody().get("status"));
        assertEquals("No booking found for the given ID", response.getBody().get("Error occured: "));
    }

    @Test
    public void testCompleteBooking_GenericException() {
        // Arrange
        Long bookingId = 1L;
        String username = "testuser@example.com";
        when(bookingService.updateBookingStatusToCompleted(bookingId, username))
                .thenThrow(new RuntimeException("Unexpected error"));

        // Act
        ResponseEntity<Map<String, Object>> response = bookingController.completeBooking(bookingId, username);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("fail", response.getBody().get("status"));
        assertEquals("Error occurred: Unexpected error", response.getBody().get("message"));
    }





    @Test
    public void testCancelBooking_GenericException() {
        // Arrange
        Long bookingId = 1L;
        doThrow(new RuntimeException("Unexpected error")).when(bookingService).cancelBooking(bookingId);

        // Act
        ResponseEntity<Map<String, Object>> response = bookingController.cancelBooking(bookingId);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("fail", response.getBody().get("status"));
        assertEquals("Error occurred: Unexpected error", response.getBody().get("message"));
    }



    @Test
    void testGetBookingDetails_Exception() {
        // Arrange
        when(bookingService.getAllBookingDetails()).thenThrow(new RuntimeException("Database error"));

        // Act
        ResponseEntity<Map<String, Object>> response = bookingController.getBookingDetails();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("fail", response.getBody().get("status"));
        assertEquals("Error occurred: Database error", response.getBody().get("message"));
    }


    @Test
    void testAddBookingEmailServiceException() {
        Booking booking = new Booking();
        String username = "testuser";

        when(bookingService.createBooking(booking, username)).thenReturn(booking);
        doThrow(new RuntimeException("Email service error")).when(emailService).sendEmail(anyString(), anyString(), anyString());

        ResponseEntity<Map<String, Object>> response = bookingController.addBooking(booking, username);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("fail", response.getBody().get("status"));
        assertEquals("Error occurred: Email service error", response.getBody().get("message"));
    }

    @Test
    void testGetBookingCountInvalidPincodeException() {
        long invalidPincode = -1L; // Invalid pincode

        // Use doThrow to set up the exception when the service method is called
        doThrow(new InvalidPincodeException("Invalid pincode"))
                .when(bookingService).getBookingCountForPincode(invalidPincode);

        // Call the controller method with the invalid pincode
        ResponseEntity<Map<String, Object>> response = bookingController.getBookingCount(invalidPincode);

        // Assert that the response status is 400 BAD_REQUEST
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        // Assert that the response body contains the correct error message
        assertEquals("fail", response.getBody().get("status"));
        assertEquals("An unexpected error occurred: Invalid pincode", response.getBody().get("message"));
    }

}
