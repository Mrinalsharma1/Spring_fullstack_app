package com.happiest.servicecenter.service;

import com.happiest.servicecenter.constant.PredefinedConstants;
import com.happiest.servicecenter.dao.ServiceCenterInterface;
import com.happiest.servicecenter.dao.UserInterface;
import com.happiest.servicecenter.dto.ServiceCenterEntity;
import com.happiest.servicecenter.dto.UserEntity;
import com.happiest.servicecenter.exception.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ServiceCenterServiceTest {

    @Mock
    private ServiceCenterInterface serviceCenterInterface;

    @Mock
    private UserInterface userInterface;

    @InjectMocks
    private ServiceCenterService serviceCenterService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddServiceCenter_Success() throws Exception {
        ServiceCenterEntity serviceCenter = new ServiceCenterEntity();
        serviceCenter.setPincode(123456L);

        when(serviceCenterInterface.findById(123456L)).thenReturn(Optional.empty());
        when(serviceCenterInterface.save(serviceCenter)).thenReturn(serviceCenter);

        ServiceCenterEntity result = serviceCenterService.addServiceCenter(serviceCenter);

        assertNotNull(result);
        assertEquals(123456L, result.getPincode());
        verify(serviceCenterInterface, times(1)).save(serviceCenter);
    }

    @Test
    public void testAddServiceCenter_AlreadyRegistered() {
        ServiceCenterEntity serviceCenter = new ServiceCenterEntity();
        serviceCenter.setPincode(123456L);

        when(serviceCenterInterface.findById(123456L)).thenReturn(Optional.of(serviceCenter));

        Exception exception = assertThrows(ServiceCenterAlreadyRegisteredException.class, () -> {
            serviceCenterService.addServiceCenter(serviceCenter);
        });

        String expectedMessage = PredefinedConstants.SERVICECENTER_ALREADY_REGISTERED + "123456";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        verify(serviceCenterInterface, never()).save(serviceCenter);
    }

    @Test
    public void testGetAllServiceCenters_Success() throws Exception {
        ServiceCenterEntity serviceCenter1 = new ServiceCenterEntity();
        ServiceCenterEntity serviceCenter2 = new ServiceCenterEntity();

        when(serviceCenterInterface.findAll()).thenReturn(Arrays.asList(serviceCenter1, serviceCenter2));

        List<ServiceCenterEntity> result = serviceCenterService.getAllServiceCenters();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(serviceCenterInterface, times(1)).findAll();
    }

    @Test
    public void testGetAllServiceCenters_NoServiceCenters() {
        when(serviceCenterInterface.findAll()).thenReturn(Arrays.asList());

        Exception exception = assertThrows(NoServiceCenterRegisteredException.class, () -> {
            serviceCenterService.getAllServiceCenters();
        });

        String expectedMessage = PredefinedConstants.NO_SERVICECENTER;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        verify(serviceCenterInterface, times(1)).findAll();
    }

    @Test
    public void testGetAllPincodes_Success() throws Exception {
        when(serviceCenterInterface.findAllPincodes()).thenReturn(Arrays.asList(123456L, 654321L));

        List<Long> result = serviceCenterService.getAllPincodes();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(serviceCenterInterface, times(1)).findAllPincodes();
    }

//    @Test
//    public void testGetAllPincodes_NoPincodes() {
//        // Arrange
//        when(serviceCenterInterface.findAllPincodes()).thenReturn(Arrays.asList());
//
//        // Act & Assert
//        NoServiceCenterRegisteredException exception = assertThrows(NoServiceCenterRegisteredException.class, () -> {
//            serviceCenterService.getAllPincodes();
//        });
//
//        // Verify
//        assertEquals(PredefinedConstants.NO_SERVICECENTER, exception.getMessage());
//        verify(serviceCenterInterface, times(1)).findAllPincodes();
//        verifyNoMoreInteractions(serviceCenterInterface);
//    }


    @Test
    public void testGetPincodeByManagerId_Success() {
        UserEntity manager = new UserEntity();
        manager.setId(1L);

        when(userInterface.findById(1L)).thenReturn(Optional.of(manager));
        when(serviceCenterInterface.findPincodeByManagerId(1L)).thenReturn(123456L);

        Long result = serviceCenterService.getPincodeByManagerId(1L);

        assertNotNull(result);
        assertEquals(123456L, result);
        verify(userInterface, times(1)).findById(1L);
        verify(serviceCenterInterface, times(1)).findPincodeByManagerId(1L);
    }

//    @Test
//    public void testGetPincodeByManagerId_ManagerNotFound() {
//        when(userInterface.findById(1L)).thenReturn(Optional.empty());
//
//        Exception exception = assertThrows(ManagerNotFoundException.class, () -> {
//            serviceCenterService.getPincodeByManagerId(1L);
//        });
//
//        String expectedMessage = PredefinedConstants.MANAGER_NOT_FOUND + "1";
//        String actualMessage = exception.getMessage();
//
//        asserStTrue(actualMessage.contains(expectedMessage));
//        verify(userInterface, times(1)).findById(1L);
//        verify(serviceCenterInterface, never()).findPincodeByManagerId(1L);
//    }

    @Test
    public void testDetachManagerFromServiceCenter_Success() throws Exception {
        ServiceCenterEntity serviceCenter = new ServiceCenterEntity();
        serviceCenter.setPincode(123456L);
        serviceCenter.setManagerId(1L);

        when(serviceCenterInterface.findById(123456L)).thenReturn(Optional.of(serviceCenter));
        when(serviceCenterInterface.save(serviceCenter)).thenReturn(serviceCenter);

        ServiceCenterEntity result = serviceCenterService.detachManagerFromServiceCenter(123456L);

        assertNotNull(result);
        assertNull(result.getManagerId());
        verify(serviceCenterInterface, times(1)).findById(123456L);
        verify(serviceCenterInterface, times(1)).save(serviceCenter);
    }

    @Test
    public void testDetachManagerFromServiceCenter_NoManagerAssigned() {
        ServiceCenterEntity serviceCenter = new ServiceCenterEntity();
        serviceCenter.setPincode(123456L);

        when(serviceCenterInterface.findById(123456L)).thenReturn(Optional.of(serviceCenter));

        Exception exception = assertThrows(NoManagerAssignedException.class, () -> {
            serviceCenterService.detachManagerFromServiceCenter(123456L);
        });

        String expectedMessage = PredefinedConstants.NO_MANAGER_ASSIGNED;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        verify(serviceCenterInterface, times(1)).findById(123456L);
        verify(serviceCenterInterface, never()).save(serviceCenter);
    }

    
    @Test
    public void testCountServiceCenters_Success() {
        when(serviceCenterInterface.count()).thenReturn(10L);

        long result = serviceCenterService.countServiceCenters();

        assertEquals(10L, result);
        verify(serviceCenterInterface, times(1)).count();
    }
}
