package com.happiest.userservice.service;

import com.happiest.userservice.constant.PredefinedConstants;
import com.happiest.userservice.dao.UserInterface;
import com.happiest.userservice.dto.UserEntity;
import com.happiest.userservice.exception.UserAlreadyRegistered;
import com.happiest.userservice.exception.UserNotRegistered;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//import org.happiest.Constant.PredefinedConstants;
//import org.happiest.Exception.StudentAlreadyRegistered;
//import org.happiest.Exception.StudentNotRegistered;
//import org.happiest.dao.StudentInterface;
//import org.happiest.dto.UserEntity;
//import org.happiest.utility.RBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserService {

    @Autowired
    UserInterface userInterface;

    private static final Logger LOGGER = LogManager.getLogger(UserService.class);

    public UserEntity register(UserEntity user) {
        try {
            LOGGER.info(PredefinedConstants.LOGGER_REGISTER_INFO1 + user.getUsername());

            Optional<UserEntity> existingUser = userInterface.findByUsername(user.getUsername());

            if (existingUser.isPresent()) {

                LOGGER.error(PredefinedConstants.LOGGER_REGISTER_ERROR+ user.getUsername()  );
                throw new UserAlreadyRegistered();
            } else {
                LOGGER.info( PredefinedConstants.LOGGER_REGISTER_INFO2 + user.getUsername() );
                return userInterface.save(user);
            }

        } catch (UserAlreadyRegistered e) {
            throw new RuntimeException(e);
        }
    }

    public UserEntity findByUsername(String email) {
        return userInterface.findByUsername(email).orElse(null);
    }

    public UserEntity findByResetToken(String token) {
        return userInterface.findByResetToken(token).orElse(null);
    }

    public UserEntity save(UserEntity user) {
        return userInterface.save(user);
    }

    public UserEntity findUserById(Long id) {
        try {
            LOGGER.info(PredefinedConstants.LOGGER_REGISTER_FETCH_INFO1 + id);  // Log the ID being fetched
            Optional<UserEntity> userOptional = userInterface.findById(id);

            if (userOptional.isPresent()) {

                LOGGER.info(PredefinedConstants.LOGGER_REGISTER_FETCH_INFO2);
                return userOptional.get(); // Return the user if present
            } else {
                // Handle the case where user is not found
                LOGGER.error(PredefinedConstants.LOGGER_REGISTER_FETCH_ERROR1 + id);
                throw new UserNotRegistered();// Throw custom exception with message
            }
        } catch (Exception e) {
            // Log the exception
            LOGGER.error(PredefinedConstants.LOGGER_REGISTER_FETCH_ERRO2 + e.getMessage());
            throw new RuntimeException(PredefinedConstants.USER_NOT_ADDED,e); // Rethrow or handle appropriately
        }
    }


    public UserEntity updateUserProfile(Long id, String newProfileName, String newEmail) {
        try {
            LOGGER.info(PredefinedConstants.LOGGER_REGISTER_UPDATE_INFO1 + id);
            Optional<UserEntity> optionalUser = userInterface.findById(id);

            if (optionalUser.isPresent()) {
                UserEntity user = optionalUser.get();
                boolean updated = false; // Flag to track if any updates were made

                if (newProfileName != null && !newProfileName.isEmpty()) {
                    user.setProfilename(newProfileName); // Update the profile name
                    updated = true; // Mark as updated
                    LOGGER.info(PredefinedConstants.LOGGER_REGISTER_UPDATE_INFO2+ newProfileName);
                }
                if (newEmail != null && !newEmail.isEmpty()) {
                    user.setUsername(newEmail); // Update the email
                    updated = true; // Mark as updated
                    LOGGER.info(PredefinedConstants.LOGGER_REGISTER_UPDATE_INFO3+ newEmail);
                }

                if (updated) {
                    return userInterface.save(user); // Save and return updated user
                } else {
                    LOGGER.warn(PredefinedConstants.LOGGER_REGISTER_UPDATE_WARN+ id);
                    return user; // Return the user as is if no updates were made
                }
            } else {
                LOGGER.error(PredefinedConstants.LOGGER_REGISTER_UPDATE_ERROR1 + id);
                throw new UserNotRegistered(PredefinedConstants.NO_USER_REGISTERED + id); // Throw exception if user not found
            }
        } catch (Exception e) {
            LOGGER.error(PredefinedConstants.LOGGER_REGISTER_UPDATE_ERROR2 + e.getMessage(), e);
            throw new RuntimeException(PredefinedConstants.USER_NOT_UPDATED, e); // Rethrow or handle appropriately
        }
    }

    public long countUsersByRole(String role) {
        try {
            return userInterface.countByRole(role);
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while counting users: " + e.getMessage(), e);
        }
    }

    public long countManagersByRole(String role) {
        try {
            return userInterface.countByRole(role);
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while counting managers: " + e.getMessage(), e);
        }
    }


}