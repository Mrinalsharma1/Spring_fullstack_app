package com.happiest.bookingservice.service;

import com.happiest.bookingservice.dao.ServiceCenterInterface;
import com.happiest.bookingservice.dao.SlotInterface;
import com.happiest.bookingservice.dto.Slot;
import com.happiest.bookingservice.dto.TimingResponse2;
import com.happiest.bookingservice.dto.UpdateSlotTimingRequest;
import com.happiest.bookingservice.exception.NoTimingsFoundException;
import com.happiest.bookingservice.exception.SlotAlreadyAddedException;
import com.happiest.bookingservice.exception.SlotNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SlotServiceTest {

    @Mock
    private SlotInterface slotInterface;

    @Mock
    private ServiceCenterInterface serviceCenterInterface;

    @InjectMocks
    private SlotService slotService;

    private Slot slot;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        slot = new Slot();
        slot.setSlotid(1L);
        slot.setTiming("10:00 AM");
    }

    @Test
    void addSlot_ShouldAddSlotSuccessfully() {
        when(slotInterface.findById(slot.getSlotid())).thenReturn(Optional.empty());
        when(slotInterface.save(slot)).thenReturn(slot);

        Slot result = slotService.addSlot(slot);

        assertEquals(slot, result);
        verify(slotInterface).save(slot);
    }

    @Test
    void addSlot_ShouldThrowSlotAlreadyAddedException_WhenSlotExists() {
        when(slotInterface.findById(slot.getSlotid())).thenReturn(Optional.of(slot));

        assertThrows(SlotAlreadyAddedException.class, () -> slotService.addSlot(slot));
        verify(slotInterface, never()).save(any(Slot.class));
    }

    @Test
    void getAvailableSlots_ShouldReturnCountOfAvailableSlots() {
        long pincode = 123456;
        String date = "2024-10-28";
        long expectedCount = 5;

        when(slotInterface.countAvailableSlots(pincode, date)).thenReturn(expectedCount);

        long result = slotService.getAvailableSlots(pincode, date);

        assertEquals(expectedCount, result);
        verify(slotInterface).countAvailableSlots(pincode, date);
    }

    @Test
    void getTimingsByPincode_ShouldReturnTimings_WhenTimingsExist() {
        long pincode = 123456;
        TimingResponse2 timing = new TimingResponse2();
        timing.setTiming("10:00 AM");
        List<TimingResponse2> timings = List.of(timing);

        when(slotInterface.findByPincode(pincode)).thenReturn(timings);

        List<TimingResponse2> result = slotService.getTimingsByPincode(pincode);

        assertEquals(timings, result);
        verify(slotInterface).findByPincode(pincode);
    }

    @Test
    void getTimingsByPincode_ShouldThrowNoTimingsFoundException_WhenNoTimingsExist() {
        long pincode = 123456;

        when(slotInterface.findByPincode(pincode)).thenReturn(Collections.emptyList());

        assertThrows(NoTimingsFoundException.class, () -> slotService.getTimingsByPincode(pincode));
        verify(slotInterface).findByPincode(pincode);
    }

    @Test
    void updateSlotTiming_ShouldUpdateTimingSuccessfully() {
        Long slotId = 1L;
        UpdateSlotTimingRequest request = new UpdateSlotTimingRequest();
        request.setNewTiming("11:00 AM");

        when(slotInterface.findById(slotId)).thenReturn(Optional.of(slot));
        when(slotInterface.save(any(Slot.class))).thenReturn(slot);

        Slot updatedSlot = slotService.updateSlotTiming(slotId, request);

        assertEquals("11:00 AM", updatedSlot.getTiming());
        verify(slotInterface).save(slot);
    }

    @Test
    void updateSlotTiming_ShouldThrowSlotNotFoundException_WhenSlotDoesNotExist() {
        Long slotId = 1L;
        UpdateSlotTimingRequest request = new UpdateSlotTimingRequest();
        request.setNewTiming("11:00 AM");

        when(slotInterface.findById(slotId)).thenReturn(Optional.empty());

        assertThrows(SlotNotFoundException.class, () -> slotService.updateSlotTiming(slotId, request));
        verify(slotInterface, never()).save(any(Slot.class));
    }
}
