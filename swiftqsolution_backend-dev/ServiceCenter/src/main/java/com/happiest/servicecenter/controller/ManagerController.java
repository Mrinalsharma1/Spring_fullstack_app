package com.happiest.servicecenter.controller;

import com.happiest.servicecenter.dao.UserInterface;
import com.happiest.servicecenter.dto.CenterInfo;
import com.happiest.servicecenter.dto.ServiceCenterEntity;
import com.happiest.servicecenter.dto.UserEntity;
import com.happiest.servicecenter.exception.*;
import com.happiest.servicecenter.service.EmailService;
import com.happiest.servicecenter.service.ManagerService;
import com.happiest.servicecenter.service.ServiceCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RequestMapping("/manager")
@RestController
public class ManagerController {
    @Autowired
    private ManagerService managerService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserInterface userInterface;

    @Autowired
    ServiceCenterService serviceCenterService;


    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    @GetMapping("/searchuser/{email}")

    public ResponseEntity<Map<String, Object>> searchUserByEmail(@PathVariable String email) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<UserEntity> user = managerService.findUserByEmailAndCheckRole(email);

            if (user.isPresent()) {
                response.put("status", "success");
                response.put("message", "User found");
                response.put("user", user.get());
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("status", "fail");
                response.put("message", "User not found");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        }
        catch (Exception e) {
            response.put("status", "fail");
            response.put("message", "Error occurred: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR); // Internal Server Error
        }
    }

    @PutMapping("/changeRole/{id}")
    public ResponseEntity<Map<String, Object>> changeUserRoleToManager(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            UserEntity updatedUser = managerService.updateUserRoleToManager(id);

            // Prepare the email details
            String subject = "Role Change Notification: Manager";
            String text = "Dear " + updatedUser.getUsername() + ",\n\n" +
                    "Congratulations! Your role has been changed to Manager.\n" +
                    "Your login details are as follows:\n" +
                    "Username: " + updatedUser.getUsername() + "\n" + // Assuming email is used as the username
                    "Password: " + updatedUser.getPassword() + "\n\n" + // Ensure to encrypt passwords and avoid exposing them in plaintext
                    "Best regards,\nService Center Team";

            // Send email using the EmailService
            emailService.sendEmail(updatedUser.getUsername(), subject, text);

            response.put("status", "success");
            response.put("message", "User role updated to manager");
            response.put("user", updatedUser);


            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (RoleCannotBeChangedException e) {
            response.put("status", "fail");
            response.put("message", "Error occurred: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); // Use NOT_FOUND (404) status
        }
        catch (NoUserFoundException e) {
            response.put("status", "fail");
            response.put("message", "Error occurred:  " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); // Use NOT_FOUND (404) status
        }
//
        catch (Exception e) {
            response.put("status", "fail");
            response.put("message", "Error occurred: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/no-manager")
    public ResponseEntity<Map<String, Object>> getServiceCentersWithNoManager() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<ServiceCenterEntity> serviceCenters = managerService.getServiceCentersWithNoManager();
            response.put("status", "success");
            response.put("serviceCenters", serviceCenters);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (NoServiceCenterWithoutManagerException e) {
            response.put("status", "fail");
            response.put("message", "Error occurred: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); // Use NOT_FOUND (404) status
        }
        catch (Exception e) {
            response.put("status", "fail");
            response.put("message", "Failed to retrieve service centers without manager: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/assign-manager/{pincode}/{userId}")
    public ResponseEntity<Map<String, Object>> assignManager(@PathVariable long pincode, @PathVariable long userId) {
        Map<String, Object> response = new HashMap<>();
        try {
            ServiceCenterEntity updatedServiceCenter = managerService.assignManagerToServiceCenter(pincode, userId);
            UserEntity manager = managerService.fetchUserById(userId);  // Ensure this method exists in your UserService

            // Prepare email details
            String subject = "Service Center Assignment Notification";
            String text = "Dear " + manager.getProfilename() + ",\n\n" +
                    "You have been assigned as the Manager for the following Service Center:\n\n" +
                    "Service Center Name: " + updatedServiceCenter.getName() + "\n" +
                    "Pincode: " + updatedServiceCenter.getPincode() + "\n" +
                    "City: " + updatedServiceCenter.getCity() + "\n" +
                    "State: " + updatedServiceCenter.getState() + "\n" +
                    "Address: " + updatedServiceCenter.getAddress() + "\n" +
                    "Service Type: " + updatedServiceCenter.getService_type() + "\n\n" +
                    "Best regards,\nService Center Team";

            // Send email using the EmailService
            emailService.sendEmail(manager.getUsername(), subject, text);
            response.put("status", "success");
            response.put("message", "Manager assigned successfully");
            response.put("serviceCenter", updatedServiceCenter);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ServiceCenterAlreadyHasManagerException e) {
            response.put("status", "fail");
            response.put("message", "Error occurred: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); // Use NOT_FOUND (404) status
        }

        catch (Exception e) {
            response.put("status", "fail");
            response.put("message", "Error occurred: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @GetMapping("/user-service-center-details")
    public ResponseEntity<Map<String, Object>> getUserServiceCenterDetails() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Object[]> results = managerService.getUserServiceCenterDetails();
            List<CenterInfo> responseList = new ArrayList<>();

            for (Object[] result : results) {
                CenterInfo dto = new CenterInfo();
                dto.setId((Long) result[0]);  // Assuming id is of type Long
                dto.setProfileName((String) result[1]);
                dto.setPincode((Long) result[2]);
                dto.setAddress((String) result[3]);
                dto.setCity((String) result[4]);
                dto.setState((String) result[5]);
//                dto.setManagerId((Long) result[6]);
                responseList.add(dto);
            }

            response.put("status", "success");
            response.put("data", responseList);  // Add the retrieved data to the response
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("status", "fail");
            response.put("message", "Error occurred: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
