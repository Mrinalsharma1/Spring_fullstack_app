package com.happiest.bookingservice.controller;

import com.happiest.bookingservice.dto.Slot;

import com.happiest.bookingservice.dto.TimingResponse2;
import com.happiest.bookingservice.dto.UpdateSlotTimingRequest;
import com.happiest.bookingservice.exception.InvalidPincodeException;
import com.happiest.bookingservice.exception.NoTimingsFoundException;
import com.happiest.bookingservice.exception.SlotAlreadyAddedException;
import com.happiest.bookingservice.exception.SlotNotFoundException;
import com.happiest.bookingservice.service.SlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/slot")
@CrossOrigin
public class SlotController {
    @Autowired
    private SlotService slotService;
   
    @PostMapping("/addslot")
    public ResponseEntity<?> addSlot(@RequestBody Slot slot) {
        try {
            Slot addedSlot = slotService.addSlot(slot);
            return ResponseEntity.status(HttpStatus.CREATED).body(addedSlot);
        } catch (SlotAlreadyAddedException e) {
            // Handle SlotAlreadyAddedException specifically
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (RuntimeException e) {
            // Handle other runtime exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while adding slot: " + e.getMessage());
        } catch (Exception e) {
            // Handle all other exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error: " + e.getMessage());
        }
    }


    @GetMapping("/availableslots")
    public ResponseEntity<Map<String, Object>> getAvailableSlots(
            @RequestParam("pincode") long pincode,
            @RequestParam("date") String date) {
        Map<String, Object> response = new HashMap<>();
        try {
            long availableSlots = slotService.getAvailableSlots(pincode, date);

            // Adding the available slots and a success message to the response map
            response.put("status", "success");
            response.put("availableSlots", availableSlots);
            response.put("message", "Slots retrieved successfully");

            return ResponseEntity.ok(response); // Return status 200 OK with the response body
        } catch (Exception e) {
            // Log the error (consider using a logging framework)
            System.err.println("Error while fetching available slots: " + e.getMessage());

            // Add error information to the response map
            response.put("status", "error");
            response.put("message", "Error while fetching available slots");

            return ResponseEntity.status(500).body(response); // Internal Server Error with error details
        }
    }


    @GetMapping("/pincodetimings")
    public ResponseEntity<Map<String, Object>> getTimings(@RequestParam long pincode) {
        Map<String, Object> responseBody = new HashMap<>();
        try {
            List<TimingResponse2> timings = slotService.getTimingsByPincode(pincode);

            // Prepare the success response
            responseBody.put("message", "Timings retrieved successfully");
            responseBody.put("timings", timings);
            responseBody.put("status", "success");// Include only timings and slotid
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } catch (NoTimingsFoundException e) {
            // Handle specific case when no timings are found
            responseBody.put("message", "Error occured: " + e.getMessage());
            responseBody.put("status", "fail");
            return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND); // Use NOT_FOUND (404) status
        } catch (Exception e) {
            responseBody.put("message", "Error occured: " + e.getMessage());
            responseBody.put("status", "fail");
            return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR); // Use INTERNAL_SERVER_ERROR (500) status
        }
    }



    @PutMapping("/updatetime/{slotId}")
    public ResponseEntity<Map<String, Object>> updateSlotTiming(
            @PathVariable Long slotId,
            @RequestBody UpdateSlotTimingRequest request) {
        Map<String, Object> responseBody = new HashMap<>();// Accept new timing in the request body
        try {
            // Call the service method to update the timing
            Slot updatedSlot = slotService.updateSlotTiming(slotId,request);
            responseBody.put("message", "Slot updated successfully");

            responseBody.put("status", "success");// Include only timings and slotid
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } catch (SlotNotFoundException e) {
            responseBody.put("message", "Error occured: " + e.getMessage());
            responseBody.put("status", "fail");
            return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            responseBody.put("message", "Error occured: " + e.getMessage());
            responseBody.put("status", "fail");
            return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/find-slot-id")
    public ResponseEntity<Map<String, Object>> findSlotId(
            @RequestParam Long pincode,
            @RequestParam String timing) {
        Map<String, Object> response = new HashMap<>();
        try {
            Long slotId = slotService.getSlotIdByPincodeAndTiming(pincode, timing);

            response.put("message", "Slot ID retrieved successfully");
            response.put("status", "success");
            response.put("slotId", slotId);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (SlotNotFoundException e) {
            response.put("message", e.getMessage());
            response.put("status", "fail");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            response.put("message", "Error occurred: " + e.getMessage());
            response.put("status", "fail");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
