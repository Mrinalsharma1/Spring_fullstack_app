package com.happiest.apigateway.apigateway.servicecenter.repository;

import com.happiest.apigateway.apigateway.bookingservice.model.ServiceCenterEntity;
import com.happiest.apigateway.apigateway.servicecenter.model.ServiceCenter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name="http://ServiceCenter/service-center")
public interface ServiceCenterInterface {

        @PostMapping("/addservicecenter")
        public ResponseEntity<Map<String, Object>> addServiceCenter(@RequestBody ServiceCenterEntity serviceCenter);
        @GetMapping("/getallservicecenters")
        public ResponseEntity<Map<String, Object>> getAllServiceCenters();
        @GetMapping("/pincode/{managerId}")
        public ResponseEntity<Map<String, Object>> getPincodeByManagerId(@PathVariable long managerId);
        @DeleteMapping("/detach-manager/{pincode}")
        public ResponseEntity<Map<String, Object>> detachManagerFromServiceCenter(@PathVariable long pincode);
        @GetMapping("/managers-details")
        public ResponseEntity<Map<String, Object>> getAllCentersWithManagerInfo();

        @GetMapping("/servicecenters/count")
        public ResponseEntity<Map<String, Object>> countServiceCenters();




}
