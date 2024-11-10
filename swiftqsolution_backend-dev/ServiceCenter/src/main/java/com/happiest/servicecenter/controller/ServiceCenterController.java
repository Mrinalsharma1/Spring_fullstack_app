package com.happiest.servicecenter.controller;

import com.happiest.servicecenter.constant.PredefinedConstants;
import com.happiest.servicecenter.dto.CenterInfo;
import com.happiest.servicecenter.dto.ServiceCenterEntity;
import com.happiest.servicecenter.exception.*;
import com.happiest.servicecenter.service.ManagerService;
import com.happiest.servicecenter.service.ServiceCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/service-center")
@CrossOrigin
public class ServiceCenterController {

    @Autowired
    private ServiceCenterService serviceCenterService;

    @Autowired
    private ManagerService managerService;

    // POST endpoint to add a new service center
    @PostMapping("/addservicecenter")
    public ResponseEntity<Map<String, Object>> addServiceCenter(@RequestBody ServiceCenterEntity serviceCenter) {
        Map<String, Object> response = new HashMap<>();
        try {

            ServiceCenterEntity savedServiceCenter = serviceCenterService.addServiceCenter(serviceCenter);


            response.put("status", "success");
            response.put("message", PredefinedConstants.SERVICECENTER_ADD_SUCCESS);
            response.put("serviceCenter", savedServiceCenter);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (ServiceCenterAlreadyRegisteredException e) {
            response.put("status", "fail");
            response.put("message", PredefinedConstants.SERVICECENTER_ALREADY_REGISTERED+ e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
   }

        catch (Exception e) {
            response.put("status", "fail");
            response.put("message", PredefinedConstants.SERVICECENTER_ADD_FAIL + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/getallservicecenters")
    public ResponseEntity<Map<String, Object>> getAllServiceCenters() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<ServiceCenterEntity> serviceCenters = serviceCenterService.getAllServiceCenters();
            response.put("status", "success");
            response.put("message", PredefinedConstants.SERVICECENTER_FETCH_SUCCESS);
            response.put("serviceCenters", serviceCenters);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (NoServiceCenterRegisteredException e) {
            // Handle the specific case when the service center is already registered
            response.put("status", "fail");
            response.put("message", PredefinedConstants.NO_SERVICECENTER + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.CONFLICT); // Use CONFLICT (409) status
        }
        catch (Exception e) {
            response.put("status", "fail");
            response.put("message", PredefinedConstants.SERVICECENTER_FETCH_FAIL + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getpincodes")
    public ResponseEntity<Map<String, Object>> getAllPincodes() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Long> pincodes = serviceCenterService.getAllPincodes();

            // Check if the list of pincodes is empty
            if (pincodes.isEmpty()) {
                response.put("status", "fail");
                response.put("message", PredefinedConstants.NO_SERVICECENTER);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); // Use NOT_FOUND (404) status
            }

            response.put("status", "success");
            response.put("pincodes", pincodes);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (NoServiceCenterRegisteredException e) {
            // Handle the specific case when no service centers are registered
            response.put("status", "fail");
            response.put("message", "No service center registered: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.CONFLICT); // Use CONFLICT (409) status
        } catch (Exception e) {
            // Log the error (consider using a logging framework)
            System.err.println("Error while fetching pincodes: " + e.getMessage());
            response.put("status", "fail");
            response.put("message", "Failed to retrieve pincodes: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR); // Internal Server Error
        }
    }

    @GetMapping("/pincode/{managerId}")
    public ResponseEntity<Map<String, Object>> getPincodeByManagerId(@PathVariable long managerId) {
        Map<String, Object> response = new HashMap<>();
        try {
            Long pincode = serviceCenterService.getPincodeByManagerId(managerId); // Now gets a single pincode

            response.put("status", "success");
            response.put("pincode", pincode); // Return a single pincode
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (ManagerNotFoundException e) {
            response.put("status", "fail");
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); // 404 Not Found
        } catch (Exception e) {
            response.put("status", "fail");
            response.put("message", "Error occurred: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR); // 500 Internal Server Error
        }
    }

    @DeleteMapping("/detach-manager/{pincode}")
    public ResponseEntity<Map<String, Object>> detachManagerFromServiceCenter(@PathVariable long pincode) {
        Map<String, Object> response = new HashMap<>();
        try {
            serviceCenterService.detachManagerFromServiceCenter(pincode);
            response.put("status", "success");
            response.put("message", "manager removed successfully"); // Return a single pincode
            return new ResponseEntity<>(response, HttpStatus.OK);


        } catch (NoManagerAssignedException e) {
            response.put("status", "fail");
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            response.put("status", "fail");
            response.put("message", "Error occurred: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/servicecenters/count")
    public ResponseEntity<Map<String, Object>> countServiceCenters() {
        Map<String, Object> responseBody = new HashMap<>();

        try {
            long count = serviceCenterService.countServiceCenters();
            responseBody.put("count", count);
            responseBody.put("message", "Service center count retrieved successfully");
            responseBody.put("status", "success");
            return ResponseEntity.ok(responseBody);
        } catch (RuntimeException e) {
            responseBody.put("message", "An error occurred: " + e.getMessage());
            responseBody.put("status", "fail");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
        }
    }






}
