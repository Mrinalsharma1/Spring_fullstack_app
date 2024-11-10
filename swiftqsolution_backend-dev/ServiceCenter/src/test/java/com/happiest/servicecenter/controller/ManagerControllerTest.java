package com.happiest.servicecenter.controller;

import com.happiest.servicecenter.dto.UserEntity;
import com.happiest.servicecenter.exception.NoServiceCenterWithoutManagerException;
import com.happiest.servicecenter.exception.NoUserFoundException;
import com.happiest.servicecenter.exception.RoleCannotBeChangedException;
import com.happiest.servicecenter.exception.ServiceCenterAlreadyHasManagerException;
import com.happiest.servicecenter.service.EmailService;
import com.happiest.servicecenter.service.ManagerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.verification.VerificationMode;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static reactor.retry.Repeat.times;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class ManagerControllerTest {

    @Mock
    private ManagerService managerService;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private ManagerController managerController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(managerController).build();
    }

//
//    @Test
//    void testSearchUserByEmail_Success() throws Exception {
//        // Create a mock user object
//        UserEntity user = new UserEntity();
//        user.setUsername("testuser");
//        user.setEmail("test@example.com");
//
//        // Configure the service to return an Optional of the user
//        when(managerService.findUserByEmailAndCheckRole("test@example.com"))
//                .thenReturn(Optional.of(user));
//
//        // Perform the GET request and verify the response
//        mockMvc.perform(get("/searchUserByEmail/test@example.com")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.status").value("success"))
//                .andExpect(jsonPath("$.message").value("User found"))
//                .andExpect(jsonPath("$.user.username").value("testuser"))
//                .andExpect(jsonPath("$.user.email").value("test@example.com"))
//                .andDo(print());
//
//        // Verify that the managerService method was called once
//        verify(managerService, times(1)).findUserByEmailAndCheckRole("test@example.com");
//    }


//    @Test
//    void testSearchUserByEmail_Success() throws Exception {
//        UserEntity user = new UserEntity();
//        user.setUsername("testuser");
//        user.setEmail("test@example.com");
//
//        when(managerService.findUserByEmailAndCheckRole("test@example.com"))
//                .thenReturn(Optional.of(user));
//
//        mockMvc.perform(get("/searchUserByEmail/test@example.com")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())  // Expecting 200 OK status
//                .andExpect(jsonPath("$.status").value("success"))
//                .andExpect(jsonPath("$.message").value("User found"))
//                .andExpect(jsonPath("$.user.username").value("testuser"))
//                .andExpect(jsonPath("$.user.email").value("test@example.com"))
//                .andDo(print());
//
//        verify(managerService, times(1)).findUserByEmailAndCheckRole("test@example.com");
//    }



//    @Test
//    void testSearchUserByEmail_UserNotFound() throws Exception {
//        // Simulate that the user is not found
//        when(managerService.findUserByEmailAndCheckRole("test@example.com")).thenReturn(Optional.empty());
//
//        // Perform the GET request and verify the response
//        mockMvc.perform(get("/searchUserByEmail/test@example.com")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.status").value("fail"))
//                .andExpect(jsonPath("$.message").value("User not found"))
//                .andDo(print());
//
//        // Verify that the managerService method was called once
//        verify(managerService, times(1)).findUserByEmailAndCheckRole("test@example.com");
//    }
//
//    @Test
//    void testSearchUserByEmail_ServiceException() throws Exception {
//        // Simulate an exception thrown by the service
//        when(managerService.findUserByEmailAndCheckRole(any(String.class))).thenThrow(new RuntimeException("Database error"));
//
//        // Perform the GET request and verify the response
//        mockMvc.perform(get("/searchUserByEmail/test@example.com")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isInternalServerError())
//                .andExpect(jsonPath("$.status").value("fail"))
//                .andExpect(jsonPath("$.message").value(containsString("Error occurred: Database error")))
//                .andDo(print());
//
//        // Verify that the managerService method was called once
//        verify(managerService, times(1)).findUserByEmailAndCheckRole("test@example.com");
//    }

    @Test
    void testChangeUserRoleToManager_RoleCannotBeChanged() throws Exception {
        when(managerService.updateUserRoleToManager(1L)).thenThrow(new RoleCannotBeChangedException("Role cannot be changed"));

        mockMvc.perform(put("/manager/changeRole/1")  // Updated endpoint URL
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("fail"))
                .andExpect(jsonPath("$.message").value("Error occurred: Role cannot be changed"))
                .andDo(print());

        verify(managerService, times(1)).updateUserRoleToManager(1L);
        verify(emailService, never()).sendEmail(anyString(), anyString(), anyString());
    }

//    @Test
//    void testChangeUserRoleToManager_NoUserFound() throws Exception {
//        when(managerService.updateUserRoleToManager(1L)).thenThrow(new NoUserFoundException("No user found"));
//
//        mockMvc.perform(put("/manager/changeRole/1")  // Updated endpoint URL
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.status").value("fail"))
//                .andExpect(jsonPath("$.message").value("Error occurred: No user found"))
//                .andDo(print());
//
//        verify(managerService, times(1)).updateUserRoleToManager(1L);
//        verify(emailService, never()).sendEmail(anyString(), anyString(), anyString());
//    }

@Test
void testChangeUserRoleToManager_Exception() throws Exception {
    when(managerService.updateUserRoleToManager(1L)).thenThrow(new RuntimeException("Unexpected error"));

    mockMvc.perform(put("/manager/changeRole/1")  // Corrected endpoint URL
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError())
            .andExpect(jsonPath("$.status").value("fail"))
            .andExpect(jsonPath("$.message").value("Error occurred: Unexpected error"))
            .andDo(print());

    verify(managerService, (org.mockito.verification.VerificationMode) times(1)).updateUserRoleToManager(1L);
    // Removed incorrect cast to VerificationMode
    verify(emailService, never()).sendEmail(anyString(), anyString(), anyString());
}


    // Test exception handling for the changeUserRoleToManager endpoint
    @Test
    void testChangeUserRoleToManager_NoUserFoundException() throws Exception {
        long userId = 1L;

        // Mock NoUserFoundException
        when(managerService.updateUserRoleToManager(userId)).thenThrow(new NoUserFoundException("User not found"));

        // Perform PUT request and verify the response for exception handling
        mockMvc.perform(put("/manager/changeRole/{id}", userId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("fail"))
                .andExpect(jsonPath("$.message").value("Error occurred:  User not found"))  // Note the extra space after "Error occurred:"
                .andDo(print());
    }


    @Test
    void testChangeUserRoleToManager_RoleCannotBeChangedException() throws Exception {
        long userId = 2L;

        // Mock RoleCannotBeChangedException
        when(managerService.updateUserRoleToManager(userId)).thenThrow(new RoleCannotBeChangedException("Role cannot be changed"));

        // Perform PUT request and verify the response for exception handling
        mockMvc.perform(put("/manager/changeRole/{id}", userId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("fail"))
                .andExpect(jsonPath("$.message").value("Error occurred: Role cannot be changed"));
    }

    // Test exception handling for getServiceCentersWithNoManager endpoint
    @Test
    void testGetServiceCentersWithNoManager_NoServiceCenterWithoutManagerException() throws Exception {
        // Mock NoServiceCenterWithoutManagerException
        when(managerService.getServiceCentersWithNoManager()).thenThrow(new NoServiceCenterWithoutManagerException("No service centers without manager found"));

        // Perform GET request and verify the response for exception handling
        mockMvc.perform(get("/manager/no-manager"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("fail"))
                .andExpect(jsonPath("$.message").value("Error occurred: No service centers without manager found"));
    }

    // Test exception handling for assignManager endpoint
    @Test
    void testAssignManager_ServiceCenterAlreadyHasManagerException() throws Exception {
        long pincode = 123456;
        long userId = 1L;

        // Mock ServiceCenterAlreadyHasManagerException
        when(managerService.assignManagerToServiceCenter(pincode, userId))
                .thenThrow(new ServiceCenterAlreadyHasManagerException("Service center already has a manager"));

        // Perform PUT request and verify the response for exception handling
        mockMvc.perform(put("/manager/assign-manager/{pincode}/{userId}", pincode, userId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("fail"))
                .andExpect(jsonPath("$.message").value("Error occurred: Service center already has a manager"));
    }

    // Test exception handling for searchUserByEmail endpoint


    @Test
    void testGetUserServiceCenterDetails_Exception() throws Exception {
        // Mock an unexpected exception in getUserServiceCenterDetails
        when(managerService.getUserServiceCenterDetails()).thenThrow(new RuntimeException("Unexpected error"));

        // Perform GET request and verify the response for exception handling
        mockMvc.perform(get("/manager/user-service-center-details"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value("fail"))
                .andExpect(jsonPath("$.message").value("Error occurred: Unexpected error"));
    }
}
