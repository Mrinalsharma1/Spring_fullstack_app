package com.happiest.servicecenter.dao;

import com.happiest.servicecenter.dto.CenterInfo;
import com.happiest.servicecenter.dto.UserEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface UserInterface extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);




    @Query(value = "SELECT u.id, u.profilename, " +
            "CASE WHEN sc.manager_id IS NOT NULL THEN sc.pincode ELSE 0 END AS pincode, " +
            "CASE WHEN sc.manager_id IS NOT NULL THEN sc.address ELSE null END AS address, " +
            "CASE WHEN sc.manager_id IS NOT NULL THEN sc.city ELSE null END AS city, " +
            "CASE WHEN sc.manager_id IS NOT NULL THEN sc.state ELSE null END AS state " +
            "FROM users u " +
            "LEFT JOIN ServiceCenter sc ON u.id = sc.manager_id " +
            "WHERE u.role = 'manager'",
            nativeQuery = true)
    List<Object[]> findUserServiceCenterDetails();


}

