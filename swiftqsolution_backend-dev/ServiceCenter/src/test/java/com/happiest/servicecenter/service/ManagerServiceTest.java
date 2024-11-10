package com.happiest.servicecenter.service;

import com.happiest.servicecenter.dao.ServiceCenterInterface;
import com.happiest.servicecenter.dao.UserInterface;
import com.happiest.servicecenter.dto.ServiceCenterEntity;
import com.happiest.servicecenter.dto.UserEntity;
import com.happiest.servicecenter.exception.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ManagerServiceTest {

    @Mock
    private UserInterface userInterface;

    @Mock
    private ServiceCenterInterface serviceCenterInterface;

    @InjectMocks
    private ManagerService managerService;

    private UserEntity sampleUser;
    private ServiceCenterEntity sampleServiceCenter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleUser = new UserEntity();
        sampleUser.setId(1L);
        sampleUser.setUsername("testuser");
        sampleUser.setRole("user");

        sampleServiceCenter = new ServiceCenterEntity();
        sampleServiceCenter.setPincode(560001L);
    }

    @Test
    void testFindUserByEmailAndCheckRole_UserNotFound() {
        when(userInterface.findByUsername(anyString())).thenReturn(Optional.empty());

        NoUserFoundException exception = assertThrows(NoUserFoundException.class, () -> {
            managerService.findUserByEmailAndCheckRole("test@example.com");
        });

        assertEquals("User not found with email: test@example.com", exception.getMessage());
    }

    @Test
    void testFindUserByEmailAndCheckRole_AdminRole() {
        sampleUser.setRole("admin");
        when(userInterface.findByUsername(anyString())).thenReturn(Optional.of(sampleUser));

        RoleCannotBeChangedException exception = assertThrows(RoleCannotBeChangedException.class, () -> {
            managerService.findUserByEmailAndCheckRole("test@example.com");
        });

        assertEquals("Role cannot be changed for admin users.", exception.getMessage());
    }

    @Test
    void testFindUserByEmailAndCheckRole_Success() {
        when(userInterface.findByUsername(anyString())).thenReturn(Optional.of(sampleUser));

        Optional<UserEntity> result = managerService.findUserByEmailAndCheckRole("test@example.com");

        assertTrue(result.isPresent());
        assertEquals(sampleUser, result.get());
    }

    @Test
    void testUpdateUserRoleToManager_UserNotFound() {
        when(userInterface.findById(anyLong())).thenReturn(Optional.empty());

        NoUserFoundException exception = assertThrows(NoUserFoundException.class, () -> {
            managerService.updateUserRoleToManager(1L);
        });

        assertEquals("User not found with ID: 1", exception.getMessage());
    }

    @Test
    void testUpdateUserRoleToManager_Success() {
        when(userInterface.findById(anyLong())).thenReturn(Optional.of(sampleUser));
        when(userInterface.save(any(UserEntity.class))).thenReturn(sampleUser);

        UserEntity result = managerService.updateUserRoleToManager(1L);

        assertEquals("manager", result.getRole());
        verify(userInterface).save(sampleUser);
    }




    @Test
    void testGetServiceCentersWithNoManager_Success() throws Exception {
        List<ServiceCenterEntity> serviceCenters = Arrays.asList(sampleServiceCenter);
        when(serviceCenterInterface.findByManagerIdIsNull()).thenReturn(serviceCenters);

        List<ServiceCenterEntity> result = managerService.getServiceCentersWithNoManager();

        assertEquals(serviceCenters, result);
    }

    @Test
    void testGetServiceCentersWithNoManager_Exception() {
        when(serviceCenterInterface.findByManagerIdIsNull()).thenThrow(new RuntimeException("Database error"));

        Exception exception = assertThrows(Exception.class, () -> {
            managerService.getServiceCentersWithNoManager();
        });

        assertEquals("Error occurred while retrieving service centers with no manager: Database error", exception.getMessage());
    }

    @Test
    void testAssignManagerToServiceCenter_ServiceCenterNotFound() {
        when(serviceCenterInterface.findById(anyLong())).thenReturn(Optional.empty());

        ServiceCenterNotFoundException exception = assertThrows(ServiceCenterNotFoundException.class, () -> {
            managerService.assignManagerToServiceCenter(560001L, 1L);
        });

        assertEquals("Service center not found.", exception.getMessage());
    }

    @Test
    void testAssignManagerToServiceCenter_ManagerNotFound() {
        when(serviceCenterInterface.findById(anyLong())).thenReturn(Optional.of(sampleServiceCenter));
        when(userInterface.findById(anyLong())).thenReturn(Optional.empty());

        ManagerNotFoundException exception = assertThrows(ManagerNotFoundException.class, () -> {
            managerService.assignManagerToServiceCenter(560001L, 1L);
        });

        assertEquals("Manager not found.", exception.getMessage());
    }

    @Test
    void testAssignManagerToServiceCenter_ServiceCenterAlreadyHasManager() {
        sampleServiceCenter.setManagerId(1L);
        when(serviceCenterInterface.findById(anyLong())).thenReturn(Optional.of(sampleServiceCenter));

        ServiceCenterAlreadyHasManagerException exception = assertThrows(ServiceCenterAlreadyHasManagerException.class, () -> {
            managerService.assignManagerToServiceCenter(560001L, 1L);
        });

        assertEquals("This service center already has a manager assigned.", exception.getMessage());
    }

    @Test
    void testAssignManagerToServiceCenter_ManagerAlreadyAssigned() {
        when(serviceCenterInterface.findById(anyLong())).thenReturn(Optional.of(sampleServiceCenter));
        when(userInterface.findById(anyLong())).thenReturn(Optional.of(sampleUser));
        when(serviceCenterInterface.findByManagerId(anyLong())).thenReturn(Optional.of(sampleServiceCenter));

        ManagerAlreadyAssignedException exception = assertThrows(ManagerAlreadyAssignedException.class, () -> {
            managerService.assignManagerToServiceCenter(560001L, 1L);
        });

        assertEquals("This manager is already assigned to another service center.", exception.getMessage());
    }

    @Test
    void testAssignManagerToServiceCenter_Success() throws ServiceCenterNotFoundException, ManagerNotFoundException, ServiceCenterAlreadyHasManagerException, ManagerAlreadyAssignedException {
        when(serviceCenterInterface.findById(anyLong())).thenReturn(Optional.of(sampleServiceCenter));
        when(userInterface.findById(anyLong())).thenReturn(Optional.of(sampleUser));
        when(serviceCenterInterface.findByManagerId(anyLong())).thenReturn(Optional.empty());
        when(serviceCenterInterface.save(any(ServiceCenterEntity.class))).thenReturn(sampleServiceCenter);

        ServiceCenterEntity result = managerService.assignManagerToServiceCenter(560001L, 1L);

        assertEquals(sampleServiceCenter, result);
        verify(serviceCenterInterface).save(sampleServiceCenter);
    }

//    @Test
//    void testGetServiceCentersWithNoManager_NoServiceCenters() throws Exception {
//        // Arrange
//        when(serviceCenterInterface.findByManagerIdIsNull()).thenReturn(Collections.emptyList());
//
//        // Act & Assert
//        NoServiceCenterWithoutManagerException exception = assertThrows(NoServiceCenterWithoutManagerException.class, () -> {
//            managerService.getServiceCentersWithNoManager();
//        });
//
//        // Verify
//        assertEquals("No service centers without a manager found.", exception.getMessage());
//        verify(serviceCenterInterface).findByManagerIdIsNull();
//    }
//    @Test
//    void testFetchUserById_UserNotFound() {
//        // Arrange
//        when(userInterface.findById(anyLong())).thenReturn(Optional.empty());
//
//        // Act & Assert
//        NoUserFoundException exception = assertThrows(NoUserFoundException.class, () -> {
//            managerService.fetchUserById(1L);
//        });
//
//        // Verify
//        assertEquals("User not found with ID: 1", exception.getMessage());
//        verify(userInterface).findById(1L);
//    }


    @Test
    void testFetchUserById_DataAccessException() {
        when(userInterface.findById(anyLong())).thenThrow(new DataAccessException("Data access error") {});

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            managerService.fetchUserById(1L);
        });

        assertEquals("Failed to fetch user data due to data access error", exception.getMessage());
    }

    @Test
    void testFetchUserById_Exception() {
        when(userInterface.findById(anyLong())).thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            managerService.fetchUserById(1L);
        });

        assertEquals("Failed to fetch user data", exception.getMessage());
    }

    @Test
    void testFetchUserById_Success() {
        when(userInterface.findById(anyLong())).thenReturn(Optional.of(sampleUser));

        UserEntity result = managerService.fetchUserById(1L);

        assertEquals(sampleUser, result);
    }

    @Test
    void testDetachManagerFromServiceCenter_ServiceCenterNotFound() {
        when(serviceCenterInterface.findById(anyLong())).thenReturn(Optional.empty());

        ServiceCenterNotFoundException exception = assertThrows(ServiceCenterNotFoundException.class, () -> {
            managerService.detachManagerFromServiceCenter(560001L);
        });

        assertEquals("Service center not found.", exception.getMessage());
    }

    @Test
    void testDetachManagerFromServiceCenter_NoManagerAssigned() {
        when(serviceCenterInterface.findById(anyLong())).thenReturn(Optional.of(sampleServiceCenter));

        NoManagerAssignedException exception = assertThrows(NoManagerAssignedException.class, () -> {
            managerService.detachManagerFromServiceCenter(560001L);
        });

        assertEquals("This service center does not have a manager assigned.", exception.getMessage());
    }

    @Test
    void testDetachManagerFromServiceCenter_Success() throws ServiceCenterNotFoundException, NoManagerAssignedException {
        sampleServiceCenter.setManagerId(1L);
        when(serviceCenterInterface.findById(anyLong())).thenReturn(Optional.of(sampleServiceCenter));
        when(serviceCenterInterface.save(any(ServiceCenterEntity.class))).thenReturn(sampleServiceCenter);

        ServiceCenterEntity result = managerService.detachManagerFromServiceCenter(560001L);

        assertNull(result.getManagerId());
        verify(serviceCenterInterface).save(sampleServiceCenter);
    }

    @Test
    void testGetUserServiceCenterDetails_Success() {
        List<Object[]> details = Arrays.asList(new Object[][]{});
        when(userInterface.findUserServiceCenterDetails()).thenReturn(details);

        List<Object[]> result = managerService.getUserServiceCenterDetails();

        assertEquals(details, result);
    }


    @Test
    void testGetUserServiceCenterDetails_Exception() {
        when(userInterface.findUserServiceCenterDetails()).thenThrow(new RuntimeException("Database error"));

        List<Object[]> result = managerService.getUserServiceCenterDetails();

        assertTrue(result.isEmpty());
    }
}
