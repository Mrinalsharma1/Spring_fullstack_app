package com.happiest.bookingservice.controller;

import com.happiest.bookingservice.dto.Slot;
import com.happiest.bookingservice.dto.TimingResponse2;
import com.happiest.bookingservice.dto.UpdateSlotTimingRequest;
import com.happiest.bookingservice.exception.NoTimingsFoundException;
import com.happiest.bookingservice.exception.SlotAlreadyAddedException;
import com.happiest.bookingservice.exception.SlotNotFoundException;
import com.happiest.bookingservice.service.SlotService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
public class SlotControllerTest {

    @InjectMocks
    private SlotController slotController;

    @Mock
    private SlotService slotService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddSlot_Success() {
        Slot slot = new Slot();
        slot.setSlotid(1);
        slot.setTiming("10:00 AM");

        when(slotService.addSlot(any(Slot.class))).thenReturn(slot);

        ResponseEntity<?> response = slotController.addSlot(slot);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(slot, response.getBody());
        verify(slotService, times(1)).addSlot(slot);
    }

    @Test
    public void testAddSlot_SlotAlreadyAddedException() {
        Slot slot = new Slot();
        slot.setSlotid(1);
        slot.setTiming("10:00 AM");

        when(slotService.addSlot(any(Slot.class))).thenThrow(new SlotAlreadyAddedException("Slot already exists"));

        ResponseEntity<?> response = slotController.addSlot(slot);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Slot already exists", response.getBody());
        verify(slotService, times(1)).addSlot(slot);
    }

    @Test
    public void testAddSlot_RuntimeException() {
        Slot slot = new Slot();
        slot.setSlotid(1);
        slot.setTiming("10:00 AM");

        when(slotService.addSlot(any(Slot.class))).thenThrow(new RuntimeException("Runtime error"));

        ResponseEntity<?> response = slotController.addSlot(slot);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error while adding slot: Runtime error", response.getBody());
        verify(slotService, times(1)).addSlot(slot);
    }

    @Test
    public void testGetAvailableSlots_Success() {
        long pincode = 123456;
        String date = "2024-10-27";
        long availableSlots = 5;

        when(slotService.getAvailableSlots(pincode, date)).thenReturn(availableSlots);

        ResponseEntity<Map<String, Object>> response = slotController.getAvailableSlots(pincode, date);
        Map<String, Object> responseBody = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("success", responseBody.get("status"));
        assertEquals(availableSlots, responseBody.get("availableSlots"));
        assertEquals("Slots retrieved successfully", responseBody.get("message"));
    }

    @Test
    void testUpdateSlotTiming_Success() {
        UpdateSlotTimingRequest request = new UpdateSlotTimingRequest();
        Slot updatedSlot = new Slot();
        when(slotService.updateSlotTiming(anyLong(), any(UpdateSlotTimingRequest.class))).thenReturn(updatedSlot);

        ResponseEntity<Map<String, Object>> response = slotController.updateSlotTiming(1L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Slot updated successfully", response.getBody().get("message"));
        assertEquals("success", response.getBody().get("status"));
    }

    @Test
    void testUpdateSlotTiming_SlotNotFoundException() {
        UpdateSlotTimingRequest request = new UpdateSlotTimingRequest();
        doThrow(new SlotNotFoundException("Slot not found")).when(slotService).updateSlotTiming(anyLong(), any(UpdateSlotTimingRequest.class));

        ResponseEntity<Map<String, Object>> response = slotController.updateSlotTiming(1L, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Error occured: Slot not found", response.getBody().get("message"));
        assertEquals("fail", response.getBody().get("status"));
    }

    @Test
    void testUpdateSlotTiming_Exception() {
        UpdateSlotTimingRequest request = new UpdateSlotTimingRequest();
        doThrow(new RuntimeException("Unexpected error")).when(slotService).updateSlotTiming(anyLong(), any(UpdateSlotTimingRequest.class));

        ResponseEntity<Map<String, Object>> response = slotController.updateSlotTiming(1L, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error occured: Unexpected error", response.getBody().get("message"));
        assertEquals("fail", response.getBody().get("status"));
    }


    @Test
    public void testGetAvailableSlots_Exception() {
        long pincode = 123456;
        String date = "2024-10-27";

        when(slotService.getAvailableSlots(pincode, date)).thenThrow(new RuntimeException("Error fetching slots"));

        ResponseEntity<Map<String, Object>> response = slotController.getAvailableSlots(pincode, date);
        Map<String, Object> responseBody = response.getBody();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("error", responseBody.get("status"));
        assertEquals("Error while fetching available slots", responseBody.get("message"));
    }

    @Test
    public void testGetTimings_Success() {
        long pincode = 123456;
        List<TimingResponse2> timings = List.of(new TimingResponse2(/* fill in constructor args */));

        when(slotService.getTimingsByPincode(pincode)).thenReturn(timings);

        ResponseEntity<Map<String, Object>> response = slotController.getTimings(pincode);
        Map<String, Object> responseBody = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("success", responseBody.get("status"));
        assertEquals("Timings retrieved successfully", responseBody.get("message"));
        assertEquals(timings, responseBody.get("timings"));
    }

    @Test
    public void testGetTimings_NoTimingsFoundException() {
        long pincode = 123456;

        // Mock the service to throw NoTimingsFoundException
        when(slotService.getTimingsByPincode(pincode)).thenThrow(new NoTimingsFoundException("No timings found"));

        // Act
        ResponseEntity<Map<String, Object>> response = slotController.getTimings(pincode);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("fail", response.getBody().get("status"));
        assertEquals("Error occured: No timings found", response.getBody().get("message")); // Note the typo here
    }


    @Test
    public void testGetTimings_Exception() {
        long pincode = 123456;

        // Mock the service to throw a general RuntimeException
        when(slotService.getTimingsByPincode(pincode)).thenThrow(new RuntimeException("Error fetching timings"));

        // Act
        ResponseEntity<Map<String, Object>> response = slotController.getTimings(pincode);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("fail", response.getBody().get("status"));
        assertEquals("Error occured: Error fetching timings", response.getBody().get("message")); // Expected message matches corrected message
    }

    @Test
    void testAddSlot_GeneralException() {
        Slot slot = new Slot();
        doThrow(new RuntimeException("General exception")).when(slotService).addSlot(any(Slot.class));

        ResponseEntity<?> response = slotController.addSlot(slot);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error while adding slot: General exception", response.getBody());
    }




}
