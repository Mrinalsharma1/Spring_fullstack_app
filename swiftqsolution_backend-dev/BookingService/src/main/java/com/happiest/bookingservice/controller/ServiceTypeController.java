package com.happiest.bookingservice.controller;

import com.happiest.bookingservice.dto.ServiceType;
import com.happiest.bookingservice.exception.NoServicesFoundException;
import com.happiest.bookingservice.exception.ProductAlreadyAddedException;
import com.happiest.bookingservice.exception.ServiceAlreadyAddedException;
import com.happiest.bookingservice.service.ServiceTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/servicetype")
public class ServiceTypeController {
    @Autowired
    ServiceTypeService serviceTypeService;

    @PostMapping("/addservice")
    public ResponseEntity<?> addServiceType(@RequestBody ServiceType serviceType) {
        try {
            ServiceType newServiceType = serviceTypeService.addServiceType(serviceType);
            return new ResponseEntity<>(newServiceType, HttpStatus.CREATED);
        } catch (ServiceAlreadyAddedException e) {
            // If the product is already added, handle the exception and return an error response

            return new ResponseEntity<>("Error occured:", HttpStatus.CONFLICT);
        }
        catch (Exception e) {
            return new ResponseEntity<>("Error adding service type: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getallservices")
    public ResponseEntity<?> getAllServiceTypes() {
        try {
            List<ServiceType> serviceTypes = serviceTypeService.getAllServiceTypes();
            return new ResponseEntity<>(serviceTypes, HttpStatus.OK);
        } catch (NoServicesFoundException e) {
            // Return a response with HTTP 404 (Not Found)
            return new ResponseEntity<>("Error occurred: " + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error retrieving service types: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
