package com.happiest.bookingservice.service;

import com.happiest.bookingservice.dao.ServiceTypeInterface;
import com.happiest.bookingservice.dto.ServiceType;
import com.happiest.bookingservice.exception.ServiceAlreadyAddedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ServiceTypeServiceTest {

    @Mock
    private ServiceTypeInterface serviceTypeInterface;

    @InjectMocks
    private ServiceTypeService serviceTypeService;

    private ServiceType serviceType;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        serviceType = new ServiceType();  // Using the default constructor
        serviceType.setServiceid(1L);
        serviceType.setServicename("Test Service");  // Setting properties individually
    }


    @Test
    public void testAddServiceTypeSuccessfully() {
        // Arrange: simulate no existing service type with the same ID
        when(serviceTypeInterface.findById(serviceType.getServiceid())).thenReturn(Optional.empty());
        when(serviceTypeInterface.save(serviceType)).thenReturn(serviceType);

        // Act
        ServiceType result = serviceTypeService.addServiceType(serviceType);

        // Assert
        assertEquals(serviceType, result);
        verify(serviceTypeInterface).save(serviceType);
    }

    @Test
    public void testAddServiceTypeAlreadyExists() {
        // Arrange: simulate existing service type with the same ID
        when(serviceTypeInterface.findById(serviceType.getServiceid())).thenReturn(Optional.of(serviceType));

        // Act & Assert
        assertThrows(ServiceAlreadyAddedException.class, () -> serviceTypeService.addServiceType(serviceType));
        verify(serviceTypeInterface, never()).save(any(ServiceType.class));
    }

    @Test
    public void testGetAllServiceTypesSuccessfully() {
        // Arrange: simulate a list of service types returned from the database
        List<ServiceType> serviceTypes = new ArrayList<>();
        serviceTypes.add(serviceType);
        when(serviceTypeInterface.findAll()).thenReturn(serviceTypes);

        // Act
        List<ServiceType> result = serviceTypeService.getAllServiceTypes();

        // Assert
        assertEquals(serviceTypes, result);
        verify(serviceTypeInterface).findAll();
    }

    @Test
    public void testGetAllServiceTypesEmptyList() {
        // Arrange: simulate an empty list of service types
        when(serviceTypeInterface.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<ServiceType> result = serviceTypeService.getAllServiceTypes();

        // Assert
        assertEquals(0, result.size());
        verify(serviceTypeInterface).findAll();
    }
}
