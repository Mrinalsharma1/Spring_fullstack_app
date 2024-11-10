package com.happiest.bookingservice.controller;

import com.happiest.bookingservice.dto.ServiceType;
import com.happiest.bookingservice.exception.NoServicesFoundException;
import com.happiest.bookingservice.exception.ServiceAlreadyAddedException;
import com.happiest.bookingservice.service.ServiceTypeService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
@ActiveProfiles("test")
class ServiceTypeControllerTest {

    @Mock
    private ServiceTypeService serviceTypeService;

    @InjectMocks
    private ServiceTypeController serviceTypeController;

    public ServiceTypeControllerTest() {
        MockitoAnnotations.initMocks(this);
    }

    // Test addServiceType - success case
    @Test
    void addServiceType_ShouldReturnCreatedStatus_WhenServiceTypeAddedSuccessfully() throws ServiceAlreadyAddedException {
        ServiceType serviceType = new ServiceType(1L, "Premium Service", "High-quality premium service");

        when(serviceTypeService.addServiceType(any(ServiceType.class))).thenReturn(serviceType);

        ResponseEntity<?> response = serviceTypeController.addServiceType(serviceType);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(serviceType, response.getBody());
        verify(serviceTypeService, times(1)).addServiceType(serviceType);
    }

    // Test addServiceType - service already added exception
    @Test
    void addServiceType_ShouldReturnConflictStatus_WhenServiceTypeAlreadyAdded() throws ServiceAlreadyAddedException {
        ServiceType serviceType = new ServiceType(1L, "Premium Service", "High-quality premium service");

        doThrow(new ServiceAlreadyAddedException("Service type already added")).when(serviceTypeService).addServiceType(any(ServiceType.class));

        ResponseEntity<?> response = serviceTypeController.addServiceType(serviceType);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Error occured:", response.getBody());
        verify(serviceTypeService, times(1)).addServiceType(serviceType);
    }

    // Test addServiceType - internal server error
    @Test
    void addServiceType_ShouldReturnInternalServerError_WhenUnexpectedErrorOccurs() throws ServiceAlreadyAddedException {
        ServiceType serviceType = new ServiceType(1L, "Premium Service", "High-quality premium service");

        doThrow(new RuntimeException("Unexpected error")).when(serviceTypeService).addServiceType(any(ServiceType.class));

        ResponseEntity<?> response = serviceTypeController.addServiceType(serviceType);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error adding service type: Unexpected error", response.getBody());
        verify(serviceTypeService, times(1)).addServiceType(serviceType);
    }

    // Test getAllServiceTypes - success case
    @Test
    void getAllServiceTypes_ShouldReturnOkStatus_WhenServiceTypesExist() throws NoServicesFoundException {
        List<ServiceType> serviceTypes = Arrays.asList(
                new ServiceType(1L, "Premium Service", "High-quality premium service"),
                new ServiceType(2L, "Basic Service", "Standard service")
        );

        when(serviceTypeService.getAllServiceTypes()).thenReturn(serviceTypes);

        ResponseEntity<?> response = serviceTypeController.getAllServiceTypes();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(serviceTypes, response.getBody());
        verify(serviceTypeService, times(1)).getAllServiceTypes();
    }

    // Test getAllServiceTypes - no services found exception
    @Test
    void getAllServiceTypes_ShouldReturnNotFoundStatus_WhenNoServiceTypesExist() throws NoServicesFoundException {
        doThrow(new NoServicesFoundException("No services found")).when(serviceTypeService).getAllServiceTypes();

        ResponseEntity<?> response = serviceTypeController.getAllServiceTypes();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Error occurred: No services found", response.getBody());
        verify(serviceTypeService, times(1)).getAllServiceTypes();
    }

    // Test getAllServiceTypes - internal server error
    @Test
    void getAllServiceTypes_ShouldReturnInternalServerError_WhenUnexpectedErrorOccurs() throws NoServicesFoundException {
        doThrow(new RuntimeException("Unexpected error")).when(serviceTypeService).getAllServiceTypes();

        ResponseEntity<?> response = serviceTypeController.getAllServiceTypes();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error retrieving service types: Unexpected error", response.getBody());
        verify(serviceTypeService, times(1)).getAllServiceTypes();
    }
}
