package com.happiest.bookingservice.service;

import com.happiest.bookingservice.dao.UserInterface;
import com.happiest.bookingservice.dto.UserEntity;
import com.happiest.bookingservice.exception.UserNotRegistered;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserInterface userInterface;

    public UserEntity fetchUserById(long id) {
        try {
            Optional<UserEntity> userOptional = userInterface.findById(id);
            if (userOptional.isPresent()) {
                return userOptional.get(); // Return the user entity if found
            } else {
                throw new UserNotRegistered("User not found with ID: " + id);
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
}
