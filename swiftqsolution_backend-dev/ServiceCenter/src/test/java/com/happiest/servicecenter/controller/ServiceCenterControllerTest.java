package com.happiest.servicecenter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.happiest.servicecenter.constant.PredefinedConstants;
import com.happiest.servicecenter.dto.ServiceCenterEntity;
import com.happiest.servicecenter.exception.*;
import com.happiest.servicecenter.service.ServiceCenterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
public class ServiceCenterControllerTest {

    @InjectMocks
    private ServiceCenterController managerController;

    private MockMvc mockMvc;

    @Mock
    private ServiceCenterService serviceCenterService;

    @InjectMocks
    private ServiceCenterController serviceCenterController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(serviceCenterController).build();
    }

    @Test
    public void testAddServiceCenter_Success() throws Exception {
        ServiceCenterEntity serviceCenter = new ServiceCenterEntity();
        when(serviceCenterService.addServiceCenter(any(ServiceCenterEntity.class))).thenReturn(serviceCenter);

        mockMvc.perform(post("/service-center/addservicecenter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(serviceCenter)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value(PredefinedConstants.SERVICECENTER_ADD_SUCCESS))
                .andExpect(jsonPath("$.serviceCenter").exists())
                .andDo(print());
    }

    @Test
    public void testAddServiceCenter_ServiceCenterAlreadyRegisteredException() throws Exception {
        ServiceCenterEntity serviceCenter = new ServiceCenterEntity();
        when(serviceCenterService.addServiceCenter(any(ServiceCenterEntity.class)))
                .thenThrow(new ServiceCenterAlreadyRegisteredException("Service center already registered"));

        mockMvc.perform(post("/service-center/addservicecenter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(serviceCenter)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value("fail"))
                .andExpect(jsonPath("$.message").value(PredefinedConstants.SERVICECENTER_ALREADY_REGISTERED + "Service center already registered"))
                .andDo(print());
    }

    @Test
    public void testAddServiceCenter_Exception() throws Exception {
        ServiceCenterEntity serviceCenter = new ServiceCenterEntity();
        when(serviceCenterService.addServiceCenter(any(ServiceCenterEntity.class)))
                .thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(post("/service-center/addservicecenter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(serviceCenter)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value("fail"))
                .andExpect(jsonPath("$.message").value(PredefinedConstants.SERVICECENTER_ADD_FAIL + "Database error"))
                .andDo(print());
    }

    @Test
    public void testGetAllServiceCenters_Success() throws Exception {
        List<ServiceCenterEntity> serviceCenters = Arrays.asList(new ServiceCenterEntity());
        when(serviceCenterService.getAllServiceCenters()).thenReturn(serviceCenters);

        mockMvc.perform(get("/service-center/getallservicecenters"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value(PredefinedConstants.SERVICECENTER_FETCH_SUCCESS))
                .andExpect(jsonPath("$.serviceCenters").isArray())
                .andDo(print());
    }

    @Test
    public void testGetAllServiceCenters_NoServiceCenterRegisteredException() throws Exception {
        when(serviceCenterService.getAllServiceCenters())
                .thenThrow(new NoServiceCenterRegisteredException("No service centers registered"));

        mockMvc.perform(get("/service-center/getallservicecenters"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value("fail"))
                .andExpect(jsonPath("$.message").value(PredefinedConstants.NO_SERVICECENTER + "No service centers registered"))
                .andDo(print());
    }

    @Test
    public void testGetAllServiceCenters_Exception() throws Exception {
        when(serviceCenterService.getAllServiceCenters())
                .thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(get("/service-center/getallservicecenters"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value("fail"))
                .andExpect(jsonPath("$.message").value(PredefinedConstants.SERVICECENTER_FETCH_FAIL + "Database error"))
                .andDo(print());
    }

    @Test
    public void testGetAllPincodes_Success() throws Exception {
        List<Long> pincodes = Arrays.asList(560001L);
        when(serviceCenterService.getAllPincodes()).thenReturn(pincodes);

        mockMvc.perform(get("/service-center/getpincodes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.pincodes").isArray())
                .andDo(print());
    }

    @Test
    public void testGetAllPincodes_EmptyList() throws Exception {
        when(serviceCenterService.getAllPincodes()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/service-center/getpincodes"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("fail"))
                .andExpect(jsonPath("$.message").value(PredefinedConstants.NO_SERVICECENTER))
                .andDo(print());
    }

    @Test
    public void testGetAllPincodes_Exception() throws Exception {
        when(serviceCenterService.getAllPincodes())
                .thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(get("/service-center/getpincodes"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value("fail"))
                .andExpect(jsonPath("$.message").value("Failed to retrieve pincodes: Database error"))
                .andDo(print());
    }

    @Test
    public void testGetPincodeByManagerId_Success() throws Exception {
        when(serviceCenterService.getPincodeByManagerId(anyLong())).thenReturn(560001L);

        mockMvc.perform(get("/service-center/pincode/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.pincode").value(560001L))
                .andDo(print());
    }

    @Test
    public void testGetPincodeByManagerId_ManagerNotFoundException() throws Exception {
        when(serviceCenterService.getPincodeByManagerId(anyLong()))
                .thenThrow(new ManagerNotFoundException("Manager not found"));

        mockMvc.perform(get("/service-center/pincode/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("fail"))
                .andExpect(jsonPath("$.message").value("Manager not found"))
                .andDo(print());
    }

    @Test
    public void testGetPincodeByManagerId_Exception() throws Exception {
        when(serviceCenterService.getPincodeByManagerId(anyLong()))
                .thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(get("/service-center/pincode/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value("fail"))
                .andExpect(jsonPath("$.message").value("Error occurred: Database error"))
                .andDo(print());
    }

    @Test
    public void testDetachManagerFromServiceCenter_Success() throws Exception {
        doAnswer(invocation -> null).when(serviceCenterService).detachManagerFromServiceCenter(anyLong());

        mockMvc.perform(delete("/service-center/detach-manager/560001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("manager removed successfully"))
                .andDo(print());
    }


    @Test
    public void testDetachManagerFromServiceCenter_NoManagerAssignedException() throws Exception {
        doThrow(new NoManagerAssignedException("No manager assigned")).when(serviceCenterService).detachManagerFromServiceCenter(anyLong());

        mockMvc.perform(delete("/service-center/detach-manager/560001"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("fail"))
                .andExpect(jsonPath("$.message").value("No manager assigned"))
                .andDo(print());
    }

    @Test
    public void testDetachManagerFromServiceCenter_Exception() throws Exception {
        doThrow(new RuntimeException("Database error")).when(serviceCenterService).detachManagerFromServiceCenter(anyLong());

        mockMvc.perform(delete("/service-center/detach-manager/560001"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value("fail"))
                .andExpect(jsonPath("$.message").value("Error occurred: Database error"))
                .andExpect(jsonPath("$.message").value("Error occurred: Database error"))
                .andDo(print());
    }

    @Test
    public void testCountServiceCenters_Success() throws Exception {
        when(serviceCenterService.countServiceCenters()).thenReturn(10L);

        mockMvc.perform(get("/service-center/servicecenters/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.count").value(10L))
                .andExpect(jsonPath("$.message").value("Service center count retrieved successfully"))
                .andDo(print());
    }

    @Test
    public void testCountServiceCenters_Exception() throws Exception {
        when(serviceCenterService.countServiceCenters()).thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(get("/service-center/servicecenters/count"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value("fail"))
                .andExpect(jsonPath("$.message").value("An error occurred: Database error"))
                .andDo(print());
    }
}

