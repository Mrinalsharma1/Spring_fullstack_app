package com.happiest.servicecenter.service;

import com.happiest.servicecenter.dao.ServiceCenterInterface;
import com.happiest.servicecenter.dao.UserInterface;
import com.happiest.servicecenter.dto.CenterInfo;
import com.happiest.servicecenter.dto.ServiceCenterEntity;
import com.happiest.servicecenter.dto.UserEntity;
import com.happiest.servicecenter.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ManagerService {
    @Autowired
    private UserInterface userInterface;
    @Autowired
    ServiceCenterInterface serviceCenterInterface;



    public Optional<UserEntity> findUserByEmailAndCheckRole(String email) {
        // Attempt to find the user by email
        Optional<UserEntity> userOptional = userInterface.findByUsername(email);

        // Check if the user exists
        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();

            // Check if the user has the role of "admin"
            if ("admin".equalsIgnoreCase(user.getRole())) {
                throw new RoleCannotBeChangedException("Role cannot be changed for admin users.");
            }

            // If the user is not an admin, return the user
            return userOptional; // Return the user if the role is not admin
        } else {
            // Handle case when user is not found
            throw new NoUserFoundException("User not found with email: " + email);
        }
    }

    public UserEntity updateUserRoleToManager(Long id) {
        Optional<UserEntity> userOptional = userInterface.findById(id);
        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();

            // Check if the user is an admin before changing the role


            // Update the user's role to "manager"
            user.setRole("manager");
            return userInterface.save(user);
        } else {
            throw new NoUserFoundException("User not found with ID: " + id);
        }
    }


    public List<ServiceCenterEntity> getServiceCentersWithNoManager() throws Exception {
        try {
            List<ServiceCenterEntity> serviceCenters = serviceCenterInterface.findByManagerIdIsNull();

            // Check if the list is empty and throw a custom exception
            if (serviceCenters.isEmpty()) {
                throw new NoServiceCenterWithoutManagerException("No service centers without a manager found.");
            }

            return serviceCenters;
        } catch (Exception e) {
            throw new Exception("Error occurred while retrieving service centers with no manager: " + e.getMessage(), e);
        }
    }

    public ServiceCenterEntity assignManagerToServiceCenter(long pincode, long userId) throws ServiceCenterNotFoundException, ManagerNotFoundException, ServiceCenterAlreadyHasManagerException, ManagerAlreadyAssignedException {
        // Find the service center by pincode
        Optional<ServiceCenterEntity> serviceCenterOpt = serviceCenterInterface.findById(pincode);
        if (serviceCenterOpt.isPresent()) {
            ServiceCenterEntity serviceCenter = serviceCenterOpt.get();

            // Check if the service center already has a manager
            if (serviceCenter.getManagerId() != null) {
                throw new ServiceCenterAlreadyHasManagerException("This service center already has a manager assigned.");
            }

            // Find the manager by userId
            Optional<UserEntity> managerOpt = userInterface.findById(userId);
            if (managerOpt.isPresent()) {
                UserEntity manager = managerOpt.get();

                // Check if the manager is already assigned to another service center
                Optional<ServiceCenterEntity> existingCenter = serviceCenterInterface.findByManagerId(manager.getId());
                if (existingCenter.isPresent()) {
                    throw new ManagerAlreadyAssignedException("This manager is already assigned to another service center.");
                }

                // Assign the manager to the service center
                serviceCenter.setManagerId(Long.valueOf(manager.getId())); // Save manager ID
                return serviceCenterInterface.save(serviceCenter); // Save changes
            } else {
                throw new ManagerNotFoundException("Manager not found.");
            }
        } else {
            throw new ServiceCenterNotFoundException("Service center not found.");
        }
    }

    public UserEntity fetchUserById(long id) {
        try {
            Optional<UserEntity> userOptional = userInterface.findById(id);
            if (userOptional.isPresent()) {
                return userOptional.get(); // Return the user entity if found
            } else {
                throw new NoUserFoundException("User not found with ID: " + id);
            }
        } catch (DataAccessException e) {
            // Log the error (using a logger framework is recommended)
            System.err.println("Data access error occurred while fetching user: " + e.getMessage());
            throw new RuntimeException("Failed to fetch user data due to data access error", e);
        } catch (Exception e) {
            // Log the error (using a logger framework is recommended)
            System.err.println("Error occurred while fetching user: " + e.getMessage());
            throw new RuntimeException("Failed to fetch user data", e);
        }
    }


    public ServiceCenterEntity detachManagerFromServiceCenter(long pincode) throws ServiceCenterNotFoundException, NoManagerAssignedException {
        // Find the service center by pincode
        Optional<ServiceCenterEntity> serviceCenterOpt = serviceCenterInterface.findById(pincode);
        if (serviceCenterOpt.isPresent()) {
            ServiceCenterEntity serviceCenter = serviceCenterOpt.get();

            // Check if the service center has a manager assigned
            if (serviceCenter.getManagerId() == null) {
                throw new NoManagerAssignedException("This service center does not have a manager assigned.");
            }

            // Detach the manager by setting managerId to null
            serviceCenter.setManagerId(null); // Remove manager ID
            return serviceCenterInterface.save(serviceCenter); // Save changes
        } else {
            throw new ServiceCenterNotFoundException("Service center not found.");
        }
    }

//    public List<Object[]> getUserServiceCenterDetails() {
//        try {
//            return userInterface.findUserServiceCenterDetails();
//        } catch (Exception e) {
//            // Log the exception (you can use a logger here)
//            System.err.println("Error fetching user and service center details: " + e.getMessage());
//            // You may want to rethrow the exception or return an empty list or handle it as needed
//            return List.of();  // Returning an empty list in case of an error
//        }
//    }


    public List<Object[]> getUserServiceCenterDetails() {
        try {
            return userInterface.findUserServiceCenterDetails();
        } catch (Exception e) {
            // Log the exception (you can use a logger here)
            System.err.println("Error fetching user and service center details: " + e.getMessage());
            // You may want to rethrow the exception or return an empty list or handle it as needed
            return List.of();  // Returning an empty list in case of an error
        }
    }










}
