package com.happiest.apigateway.apigateway.feedbackservice.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.happiest.apigateway.apigateway.feedbackservice.repository.FeedbackInterface;
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
public class FeedbackControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @MockBean
    private FeedbackInterface feedbackInterface;

    @Test
    void testGetAllFeedbacks() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        Map<String, Object> feedbacks = new HashMap<>();
        feedbacks.put("feedback1", "Great service!");

        when(feedbackInterface.getAllFeedbacks()).thenReturn(ResponseEntity.ok(feedbacks));

        mockMvc.perform(get("/getfeedbacks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feedback1").value("Great service!"))
                .andDo(print());

        // Additional test for exception handling
        when(feedbackInterface.getAllFeedbacks()).thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(get("/getfeedbacks"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("fail"))
                .andExpect(jsonPath("$.message").value("Database error"))
                .andDo(print());
    }

    @Test
    void testCountFeedbacks() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        Map<String, Object> countResponse = new HashMap<>();
        countResponse.put("count", 5);

        when(feedbackInterface.countFeedbacks(560001)).thenReturn(ResponseEntity.ok(countResponse));

        mockMvc.perform(get("/feedback/count")
                        .param("pincode", "560001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(5))
                .andDo(print());

        // Additional test for exception handling
        when(feedbackInterface.countFeedbacks(560001)).thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(get("/feedback/count")
                        .param("pincode", "560001"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("fail"))
                .andExpect(jsonPath("$.message").value("Database error"))
                .andDo(print());
    }
}
