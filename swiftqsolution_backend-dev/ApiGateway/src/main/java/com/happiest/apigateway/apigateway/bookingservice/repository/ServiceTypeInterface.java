package com.happiest.apigateway.apigateway.bookingservice.repository;


import com.happiest.apigateway.apigateway.bookingservice.model.ServiceType;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="http://BookingService/servicetype")
public interface ServiceTypeInterface{

    @PostMapping("/addservice")
    public ResponseEntity<?> addServiceType(@RequestBody ServiceType serviceType);

    @GetMapping("/getallservices")
    public ResponseEntity<?> getAllServiceTypes();
}
