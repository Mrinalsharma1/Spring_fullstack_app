package com.happiest.bookingservice.dao;

import com.happiest.bookingservice.dto.ServiceCenterEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceCenterInterface extends JpaRepository<ServiceCenterEntity, Long> {
}
