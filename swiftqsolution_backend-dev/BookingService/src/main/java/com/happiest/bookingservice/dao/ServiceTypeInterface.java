package com.happiest.bookingservice.dao;

import com.happiest.bookingservice.dto.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceTypeInterface extends JpaRepository<ServiceType , Long> {
}
