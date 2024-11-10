package com.happiest.bookingservice.dao;

import com.happiest.bookingservice.dto.Slot;

import com.happiest.bookingservice.dto.TimingResponse2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface SlotInterface extends JpaRepository<Slot, Long> {
    @Query("SELECT COUNT(s) FROM Slot s LEFT JOIN Booking b ON s = b.slot AND b.bdate = :date " +
            "WHERE s.serviceCenter.pincode = :pincode AND b IS NULL")
    long countAvailableSlots(@Param("pincode") long pincode, @Param("date") String date);

    @Query("SELECT new com.happiest.bookingservice.dto.TimingResponse2(s.slotid, s.timing) " +
            "FROM Slot s JOIN s.serviceCenter sc " +
            "WHERE sc.pincode = :pincode")
    List<TimingResponse2> findByPincode(@Param("pincode") long pincode);


    @Query("SELECT s.slotid FROM Slot s WHERE s.serviceCenter.pincode = :pincode AND s.timing = :timing")
    Optional<Long> findSlotIdByPincodeAndTiming(@Param("pincode") long pincode, @Param("timing") String timing);

}
