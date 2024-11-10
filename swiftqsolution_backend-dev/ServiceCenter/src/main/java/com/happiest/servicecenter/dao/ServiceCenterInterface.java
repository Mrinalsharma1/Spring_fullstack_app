package com.happiest.servicecenter.dao;

import com.happiest.servicecenter.dto.CenterInfo;
import com.happiest.servicecenter.dto.ServiceCenterEntity;
import com.happiest.servicecenter.exception.ServiceCenterNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ServiceCenterInterface extends JpaRepository<ServiceCenterEntity,Long> {
    List<ServiceCenterEntity> findByManagerIdIsNull();
    Optional<ServiceCenterEntity> findByManagerId(Long managerId);

    @Query("SELECT sc.pincode FROM ServiceCenterEntity sc")
    List<Long> findAllPincodes();

    @Query("SELECT sc.pincode FROM ServiceCenterEntity sc WHERE sc.managerId = ?1")
    Long findPincodeByManagerId(long managerId);

    long count();



}
