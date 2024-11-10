package com.happiest.bookingservice.dao;

import com.happiest.bookingservice.dto.Booking;
import com.happiest.bookingservice.dto.BookingResponse;
import com.happiest.bookingservice.dto.BookingResponse2;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookingInterface extends JpaRepository<Booking, Long> {
    @Query(value = "SELECT COUNT(*) FROM booking b " +
            "JOIN slot s ON b.slotid = s.slotid " +
            "JOIN servicecenter sc ON s.pincode = sc.pincode " +
            "WHERE sc.pincode = :pincode", nativeQuery = true)
    long countBookingsByPincode(@Param("pincode") long pincode);

    @Query("SELECT b FROM Booking b WHERE b.bdate BETWEEN :startDate AND :endDate")
    List<Booking> findBookingsBetweenDates(@Param("startDate") String startDate, @Param("endDate") String endDate);

    @Query("SELECT s.timing, COALESCE(b.status, null) FROM Slot s " +
            "LEFT JOIN Booking b ON s.slotid = b.slot.slotid AND b.bdate = :date " +
            "JOIN s.serviceCenter sc WHERE sc.pincode = :pincode")
    List<Object[]> findTimingsByPincodeAndDate(@Param("pincode") long pincode, @Param("date") String date);

    @Query("SELECT new com.happiest.bookingservice.dto.BookingResponse(b.bookingid, b.bdate, b.status, s.timing, s.serviceCenter.pincode, u.username) " +
            "FROM Booking b " +
            "JOIN b.slot s " +
            "JOIN b.user u")
    List<BookingResponse> findAllBookings();

    @Query("SELECT new com.happiest.bookingservice.dto.BookingResponse2(b.bookingid, b.bdate, b.user.id, b.status, s.timing, sc.name, sc.address) " +
            "FROM Booking b " +
            "JOIN b.slot s " +
            "JOIN s.serviceCenter sc")
    List<BookingResponse2> findAllBookingDetails();

    @Modifying
    @Transactional
    @Query("UPDATE Booking b SET b.status = 'completed' WHERE b.bookingid = :bookingId AND b.status = 'confirmed'")
    int updateStatusToCompleted(Long bookingId);

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.status = 'completed' AND b.slot.serviceCenter.pincode = :pincode")
    long countCompletedBookingsByPincode(@Param("pincode") Long pincode);

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.status = 'completed'")
    long countTotalCompletedBookings();








}
