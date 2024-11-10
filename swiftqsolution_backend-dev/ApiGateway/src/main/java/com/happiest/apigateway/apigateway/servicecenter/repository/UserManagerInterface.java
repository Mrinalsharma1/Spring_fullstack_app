package com.happiest.apigateway.apigateway.servicecenter.repository;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.Map;

@FeignClient(name="http://ServiceCenter/manager")
public interface UserManagerInterface {

    @GetMapping("/searchuser/{email}")

    public ResponseEntity<Map<String, Object>> searchUserByEmail(@PathVariable String email);

    @PutMapping("/changeRole/{id}")
    public ResponseEntity<Map<String, Object>> changeUserRoleToManager(@PathVariable Long id);

    @GetMapping("/no-manager")
    public ResponseEntity<Map<String, Object>> getServiceCentersWithNoManager();

    @PutMapping("/assign-manager/{pincode}/{userId}")
    public ResponseEntity<Map<String, Object>> assignManager(@PathVariable long pincode, @PathVariable long userId);

    @GetMapping("/user-service-center-details")
    public ResponseEntity<Map<String, Object>> getUserServiceCenterDetails();


}
