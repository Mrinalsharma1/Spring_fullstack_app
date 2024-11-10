package com.happiest.bookingservice.service;

import com.happiest.bookingservice.dao.ServiceCenterInterface;
import com.happiest.bookingservice.dao.SlotInterface;
import com.happiest.bookingservice.dto.*;

import com.happiest.bookingservice.exception.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class SlotService {
    @Autowired
    private SlotInterface slotInterface;

    @Autowired
    private ServiceCenterInterface serviceCenterInterface;

    private static final Logger LOGGER = LogManager.getLogger(SlotService.class);
    public Slot addSlot(Slot slot) {
        try {
            LOGGER.info("Registering slot for service center  ");

            Optional<Slot> existingSlot = slotInterface.findById(slot.getSlotid());
            if (existingSlot.isPresent()) {
                LOGGER.error("Slot already exists for the service center "
                );
                throw new SlotAlreadyAddedException("Slot already exists for  this service center.");
            }


            // Log success and save the slot with the associated service center
            LOGGER.info("Slot registered successfully for service center ");
            return slotInterface.save(slot);

        } catch (SlotAlreadyAddedException e) {
            throw e; // Re-throw custom exception

        } catch (Exception e) {
            // Log the error (consider using a logging framework)

            throw new RuntimeException("Error while adding slots: " + e.getMessage(), e);
        }
    }





    public long getAvailableSlots(long pincode, String date) {
        try {
            return slotInterface.countAvailableSlots(pincode, date);
        } catch (Exception e) {
            // Log the error (consider using a logging framework)
            System.err.println("Error while fetching available slots: " + e.getMessage());
            throw new RuntimeException("Error while fetching available slots: " + e.getMessage(), e);
        }
    }

    public List<TimingResponse2> getTimingsByPincode(long pincode) {
        try {
            List<TimingResponse2> timings = slotInterface.findByPincode(pincode);
            if (timings.isEmpty()) {
                throw new NoTimingsFoundException("No timings found for the provided pincode: " + pincode);
            }
            return timings;
        } catch (Exception e) {
            // Log the error (consider using a logging framework)
            System.err.println("Error while fetching timings from service: " + e.getMessage());
            throw e; // Re-throwing to be handled in the controller
        }
    }


//    public Slot updateSlotTiming(Long slotId, String newTiming) {
//        try {
//            LOGGER.info("Attempting to update timing for slot with ID: " + slotId);
//
//            // Check if the slot exists
//            Optional<Slot> existingSlot = slotInterface.findById(slotId);
//            if (existingSlot.isEmpty()) {
//                LOGGER.error("Slot with ID " + slotId + " does not exist.");
//                throw new SlotNotFoundException("Slot not found with ID: " + slotId);
//            }
//
//            // Update the timing
//            Slot slotToUpdate = existingSlot.get();
//            slotToUpdate.setTiming(newTiming);
//
//            // Log success and save the updated slot
//            Slot updatedSlot = slotInterface.save(slotToUpdate);
//            LOGGER.info("Slot timing for ID " + slotId + " updated successfully to: " + newTiming);
//            return updatedSlot;
//
//        } catch (SlotNotFoundException e) {
//            throw e; // Re-throw custom exception
//        } catch (Exception e) {
//            LOGGER.error("Error while updating slot timing: " + e.getMessage(), e);
//            throw new RuntimeException("Error while updating slot timing: " + e.getMessage(), e);
//        }
//    }

    public Slot updateSlotTiming(Long slotId, UpdateSlotTimingRequest request) {
        try {
            LOGGER.info("Attempting to update timing for slot with ID: " + slotId);

            // Check if the slot exists
            Optional<Slot> existingSlot = slotInterface.findById(slotId);
            if (existingSlot.isEmpty()) {
                LOGGER.error("Slot with ID " + slotId + " does not exist.");
                throw new SlotNotFoundException("Slot not found with ID: " + slotId);
            }

            // Update the timing
            Slot slotToUpdate = existingSlot.get();
            slotToUpdate.setTiming(request.getNewTiming());  // Use the newTiming from the request body

            // Log success and save the updated slot
            Slot updatedSlot = slotInterface.save(slotToUpdate);
            LOGGER.info("Slot timing for ID " + slotId + " updated successfully to: " + request.getNewTiming());
            return updatedSlot;

        } catch (SlotNotFoundException e) {
            throw e; // Re-throw custom exception
        } catch (Exception e) {
            LOGGER.error("Error while updating slot timing: " + e.getMessage(), e);
            throw new RuntimeException("Error while updating slot timing: " + e.getMessage(), e);
        }
    }

    public Long getSlotIdByPincodeAndTiming(Long pincode, String timing) {
        try {
            Optional<Long> slotId = slotInterface.findSlotIdByPincodeAndTiming(pincode, timing);
            return slotId.orElseThrow(() -> new SlotNotFoundException("No slot found for the given pincode and timing"));
        } catch (SlotNotFoundException e) {
            // Log the error and rethrow it to be handled by the controller
            System.err.println("Error: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            // Handle other unexpected exceptions
            System.err.println("Unexpected error: " + e.getMessage());
            throw new RuntimeException("An unexpected error occurred while retrieving the slot ID", e);
        }
    }




}
