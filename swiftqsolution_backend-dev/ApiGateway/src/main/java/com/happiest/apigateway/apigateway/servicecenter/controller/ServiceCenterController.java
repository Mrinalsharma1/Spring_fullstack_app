package com.happiest.apigateway.apigateway.servicecenter.controller;

import com.happiest.apigateway.apigateway.bookingservice.model.ServiceCenterEntity;
import com.happiest.apigateway.apigateway.servicecenter.model.ServiceCenter;
import com.happiest.apigateway.apigateway.servicecenter.repository.ServiceCenterInterface;
import com.happiest.apigateway.apigateway.servicecenter.repository.UserManagerInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
//@RequestMapping("/service-center")
public class ServiceCenterController {

    @Autowired
    ServiceCenterInterface serviceCenterInterface;

    @Autowired
    UserManagerInterface userManagerInterface;

//    testing endpoint start here



// ------------------   testing endpoint end here  ---------------------


// --------------------   service center endpoint start here ----------------

    @PostMapping("/addservicecenter")
    public ResponseEntity<Map<String, Object>> addServiceCenter(@RequestBody ServiceCenterEntity serviceCenter)
    {
        Map<String, Object> response = new HashMap<>();
        try {
            return serviceCenterInterface.addServiceCenter(serviceCenter);
        }
        catch(Exception e){
            response.put("status", "fail");
            response.put("message", "Failed to add service centers: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);


        }
    }

    @GetMapping("/getallservicecenters")
    public ResponseEntity<Map<String, Object>> getAllServiceCenters() {
        Map<String, Object> response = new HashMap<>();

        try{
            return serviceCenterInterface.getAllServiceCenters();
        }
        catch(Exception e){
            response.put("status", "fail");
            response.put("message", "Failed to retrieve service centers: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);


        }


    }

    @GetMapping("/pincode/{managerId}")
    public ResponseEntity<Map<String, Object>> getPincodeByManagerId(@PathVariable long managerId){
        Map<String, Object> response = new HashMap<>();

        try {
            return serviceCenterInterface.getPincodeByManagerId(managerId);
        }
        catch(Exception e){
                response.put("status", "fail");
                response.put("message", "Failed to retrieve service centers: " + e.getMessage());
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);


            }

    }

    @DeleteMapping("/detach-manager/{pincode}")
    public ResponseEntity<Map<String, Object>> detachManagerFromServiceCenter(@PathVariable long pincode){
        Map<String, Object> response = new HashMap<>();
        try{
            return serviceCenterInterface.detachManagerFromServiceCenter(pincode);
        }
        catch(Exception e){
            response.put("status", "fail");
            response.put("message", "" + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);


        }



    }
    @GetMapping("/managers-details")
    public ResponseEntity<Map<String, Object>> getAllCentersWithManagerInfo(){
        Map<String, Object> response = new HashMap<>();
        try{
            return serviceCenterInterface.getAllCentersWithManagerInfo();
        }
        catch(Exception e){
            response.put("status", "fail");
            response.put("message", "" + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);


        }

    }

    @GetMapping("/servicecenters/count")
    public ResponseEntity<Map<String, Object>> countServiceCenters(){
        Map<String, Object> response = new HashMap<>();
        try{
            return serviceCenterInterface.countServiceCenters();
        }
        catch(Exception e){
            response.put("status", "fail");
            response.put("message", "" + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);


        }


    }

    // ------------   service center endpoint end here ------------------


//    ----------------- user manager endpoint start here ---------------------

    @GetMapping("/searchuser/{email}")

    public ResponseEntity<Map<String, Object>> searchUserByEmail(@PathVariable String email) {
        Map<String, Object> response = new HashMap<>();
        try {
            return userManagerInterface.searchUserByEmail(email);
        }
        catch(Exception e){
            response.put("status", "fail");
            response.put("message", "" + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/changeRole/{id}")
    public ResponseEntity<Map<String, Object>> changeUserRoleToManager(@PathVariable Long id){
        Map<String, Object> response = new HashMap<>();
        try {
            return userManagerInterface.changeUserRoleToManager(id);
        }
        catch(Exception e){
            response.put("status", "fail");
            response.put("message",""+ e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);


        }
    }

    @GetMapping("/no-manager")
    public ResponseEntity<Map<String, Object>> getServiceCentersWithNoManager(){
        Map<String, Object> response = new HashMap<>();
        try {
            return userManagerInterface.getServiceCentersWithNoManager();
        }
        catch(Exception e){
            response.put("status", "fail");
            response.put("message", "" + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);


        }

    }

    @PutMapping("/assign-manager/{pincode}/{userId}")
    public ResponseEntity<Map<String, Object>> assignManager(@PathVariable long pincode, @PathVariable long userId){
        Map<String, Object> response = new HashMap<>();
        try{
            return userManagerInterface.assignManager(pincode, userId);
        }
        catch(Exception e){
            response.put("status", "fail");
            response.put("message", "" + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);


        }



    }

    @GetMapping("/user-service-center-details")
    public ResponseEntity<Map<String, Object>> getUserServiceCenterDetails(){
        Map<String, Object> response = new HashMap<>();
        try{
            return userManagerInterface.getUserServiceCenterDetails();
        }
        catch(Exception e){
            response.put("status", "fail");
            response.put("message", "" + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);


        }


    }




}
