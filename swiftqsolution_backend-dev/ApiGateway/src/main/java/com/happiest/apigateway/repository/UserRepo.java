package com.happiest.apigateway.repository;

import com.happiest.apigateway.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<Users, Long> {

    Users findByUsername(String name);
}

