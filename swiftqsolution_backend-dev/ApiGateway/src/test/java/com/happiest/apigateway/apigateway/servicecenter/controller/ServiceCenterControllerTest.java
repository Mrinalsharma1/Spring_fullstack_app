package com.happiest.apigateway.apigateway.servicecenter.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.happiest.apigateway.apigateway.bookingservice.model.ServiceCenterEntity;
import com.happiest.apigateway.apigateway.servicecenter.repository.ServiceCenterInterface;
import com.happiest.apigateway.apigateway.servicecenter.repository.UserManagerInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class ServiceCenterControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @MockBean
    private ServiceCenterInterface serviceCenterInterface;

    @MockBean
    private UserManagerInterface userManagerInterface;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void testAddServiceCenter() throws Exception {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");

        ServiceCenterEntity serviceCenter = new ServiceCenterEntity();
        serviceCenter.setName("Test Service Center");

        when(serviceCenterInterface.addServiceCenter(any(ServiceCenterEntity.class)))
                .thenReturn(ResponseEntity.ok(response));

        mockMvc.perform(post("/addservicecenter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Test Service Center\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andDo(print());

        // Additional test for exception handling
        when(serviceCenterInterface.addServiceCenter(any(ServiceCenterEntity.class)))
                .thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(post("/addservicecenter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Test Service Center\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("fail"))
                .andExpect(jsonPath("$.message").value("Failed to add service centers: Database error"))
                .andDo(print());
    }

    @Test
    void testGetAllServiceCenters() throws Exception {
        Map<String, Object> response = new HashMap<>();
        response.put("serviceCenter1", "Center 1");

        when(serviceCenterInterface.getAllServiceCenters()).thenReturn(ResponseEntity.ok(response));

        mockMvc.perform(get("/getallservicecenters"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.serviceCenter1").value("Center 1"))
                .andDo(print());

        // Additional test for exception handling
        when(serviceCenterInterface.getAllServiceCenters()).thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(get("/getallservicecenters"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("fail"))
                .andExpect(jsonPath("$.message").value("Failed to retrieve service centers: Database error"))
                .andDo(print());
    }

    @Test
    void testGetPincodeByManagerId() throws Exception {
        Map<String, Object> response = new HashMap<>();
        response.put("pincode", 560001);

        when(serviceCenterInterface.getPincodeByManagerId(1L)).thenReturn(ResponseEntity.ok(response));

        mockMvc.perform(get("/pincode/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pincode").value(560001))
                .andDo(print());

        // Additional test for exception handling
        when(serviceCenterInterface.getPincodeByManagerId(1L)).thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(get("/pincode/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("fail"))
                .andExpect(jsonPath("$.message").value("Failed to retrieve service centers: Database error"))
                .andDo(print());
    }

    @Test
    void testDetachManagerFromServiceCenter() throws Exception {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");

        when(serviceCenterInterface.detachManagerFromServiceCenter(560001L)).thenReturn(ResponseEntity.ok(response));

        mockMvc.perform(delete("/detach-manager/560001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andDo(print());

        // Additional test for exception handling
        when(serviceCenterInterface.detachManagerFromServiceCenter(560001L)).thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(delete("/detach-manager/560001"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("fail"))
                .andExpect(jsonPath("$.message").value("Database error"))
                .andDo(print());
    }

    @Test
    void testGetAllCentersWithManagerInfo() throws Exception {
        Map<String, Object> response = new HashMap<>();
        response.put("center1", "Manager Info");

        when(serviceCenterInterface.getAllCentersWithManagerInfo()).thenReturn(ResponseEntity.ok(response));

        mockMvc.perform(get("/managers-details"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.center1").value("Manager Info"))
                .andDo(print());

        // Additional test for exception handling
        when(serviceCenterInterface.getAllCentersWithManagerInfo()).thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(get("/managers-details"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("fail"))
                .andExpect(jsonPath("$.message").value("Database error"))
                .andDo(print());
    }

    @Test
    void testCountServiceCenters() throws Exception {
        Map<String, Object> response = new HashMap<>();
        response.put("count", 10);

        when(serviceCenterInterface.countServiceCenters()).thenReturn(ResponseEntity.ok(response));

        mockMvc.perform(get("/servicecenters/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(10))
                .andDo(print());

        // Additional test for exception handling
        when(serviceCenterInterface.countServiceCenters()).thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(get("/servicecenters/count"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("fail"))
                .andExpect(jsonPath("$.message").value("Database error"))
                .andDo(print());
    }

    @Test
    void testSearchUserByEmail() throws Exception {
        Map<String, Object> response = new HashMap<>();
        response.put("user", "User Info");

        when(userManagerInterface.searchUserByEmail("test@example.com")).thenReturn(ResponseEntity.ok(response));

        mockMvc.perform(get("/searchuser/test@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user").value("User Info"))
                .andDo(print());

        // Additional test for exception handling
        when(userManagerInterface.searchUserByEmail("test@example.com")).thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(get("/searchuser/test@example.com"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("fail"))
                .andExpect(jsonPath("$.message").value("Database error"))
                .andDo(print());
    }

    @Test
    void testChangeUserRoleToManager() throws Exception {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");

        when(userManagerInterface.changeUserRoleToManager(1L)).thenReturn(ResponseEntity.ok(response));

        mockMvc.perform(put("/changeRole/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andDo(print());

        // Additional test for exception handling
        when(userManagerInterface.changeUserRoleToManager(1L)).thenThrow(new RuntimeException("User not found"));

        mockMvc.perform(put("/changeRole/1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("fail"))
                .andExpect(jsonPath("$.message").value("User not found"))
                .andDo(print());
    }
}
