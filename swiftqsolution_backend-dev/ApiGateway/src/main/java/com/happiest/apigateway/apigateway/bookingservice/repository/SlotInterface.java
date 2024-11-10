package com.happiest.apigateway.apigateway.bookingservice.repository;


import com.happiest.apigateway.apigateway.bookingservice.model.Slot;
import com.happiest.apigateway.apigateway.bookingservice.model.UpdateSlotTimingRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name="http://BookingService/slot")
public interface SlotInterface{


    @PostMapping("/addslot")
    public ResponseEntity<?> addSlot(@RequestBody Slot slot);

    @GetMapping("/availableslots")
    public ResponseEntity<Long> getAvailableSlots(
            @RequestParam("pincode") long pincode,
            @RequestParam("date") String date);

    @GetMapping("/pincodetimings")
    public ResponseEntity<Map<String, Object>> getTimings(@RequestParam long pincode);

    @PutMapping("/updatetime/{slotId}")
    public ResponseEntity<Map<String, Object>> updateSlotTiming(
            @PathVariable Long slotId,
            @RequestBody UpdateSlotTimingRequest request);

}
