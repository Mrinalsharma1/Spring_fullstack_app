package com.happiest.apigateway.apigateway.bookingservice.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.happiest.apigateway.apigateway.bookingservice.model.*;
import com.happiest.apigateway.apigateway.bookingservice.repository.BookingInterface;
import com.happiest.apigateway.apigateway.bookingservice.repository.ProductInterface;
import com.happiest.apigateway.apigateway.bookingservice.repository.ServiceTypeInterface;
import com.happiest.apigateway.apigateway.bookingservice.repository.SlotInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;

@SpringBootTest
public class BookingServiceControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @MockBean
    private ServiceTypeInterface serviceTypeInterface;

    @MockBean
    private BookingInterface bookingInterface;

    @MockBean
    private SlotInterface slotInterface;

    @MockBean
    private ProductInterface productInterface;

    private Product sampleProduct;
    private ServiceType sampleServiceType;
    private Slot sampleSlot;
    private Booking sampleBooking;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        sampleProduct = new Product(1L, "SampleProduct");
        sampleServiceType = new ServiceType(1L, "SampleService");
        sampleSlot = new Slot(1L, "2023-12-31", "09:00 AM", "11:00 AM", 560001L, 5);
        sampleBooking = new Booking(1L, "testuser", 560001L, "2023-12-31", "09:00 AM", "Pending");
    }

    @Test
    void testAddProduct() throws Exception {
        Product sampleProduct = new Product();
        sampleProduct.setProductname("SampleProduct");

        ResponseEntity<?> response = new ResponseEntity<>(sampleProduct, HttpStatus.OK);
        when(productInterface.addProduct(any(Product.class))).thenReturn((ResponseEntity) response);

        mockMvc.perform(post("/addproduct")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productname\":\"SampleProduct\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productname").value("SampleProduct"))
                .andDo(print());

        // Additional test for exception handling
        when(productInterface.addProduct(any(Product.class))).thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(post("/addproduct")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productname\":\"SampleProduct\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("fail"))
                .andExpect(jsonPath("$.message").value("Database error"))
                .andDo(print());
    }



    @Test
    void testGetAllProducts() throws Exception {
        List<Product> products = Arrays.asList(sampleProduct);
        ResponseEntity<List<Product>> responseEntity = ResponseEntity.ok(products);
        when(productInterface.getAllProducts()).thenReturn((ResponseEntity) responseEntity);

        mockMvc.perform(get("/getallproducts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productname").value("SampleProduct"))
                .andDo(print());

        // Additional test for exception handling
        when(productInterface.getAllProducts()).thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(get("/getallproducts"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("fail"))
                .andExpect(jsonPath("$.message").value("Database error"))
                .andDo(print());
    }

    @Test
    void testCountCompletedBookings_Exception() throws Exception {
        when(bookingInterface.countCompletedBookings(anyLong()))
                .thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(get("/completed/count")
                        .param("pincode", "560001"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("fail"))
                .andExpect(jsonPath("$.message").value("Database error"))
                .andDo(print());
    }

    @Test
    void testCountTotalCompletedBookings_Exception() throws Exception {
        when(bookingInterface.countTotalCompletedBookings())
                .thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(get("/bookings/count/completed"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("fail"))
                .andExpect(jsonPath("$.message").value("Database error"))
                .andDo(print());
    }


    @Test
    void testGetBookingsBetweenDates() throws Exception {
        Map<String, Object> mockResponse = new HashMap<>();
        mockResponse.put("status", "success");
        mockResponse.put("data", new ArrayList<>());

        when(bookingInterface.getBookingsBetweenDates(anyString(), anyString()))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        mockMvc.perform(get("/between-dates")
                        .param("startDate", "2023-01-01")
                        .param("endDate", "2023-12-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"));
    }



    @Test
    void testGetAllBookings() throws Exception {
        Map<String, Object> mockResponse = new HashMap<>();
        mockResponse.put("status", "success");
        mockResponse.put("data", new ArrayList<>());

        when(bookingInterface.getAllBookings())
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        mockMvc.perform(get("/bookings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"));
    }

    @Test
    void testGetBookingDetails() throws Exception {
        Map<String, Object> mockResponse = new HashMap<>();
        mockResponse.put("status", "success");
        mockResponse.put("data", new HashMap<>());

        when(bookingInterface.getBookingDetails())
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        mockMvc.perform(get("/user-booking-details"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"));
    }

    @Test
    void testGetBookingsBetweenDates_Exception() throws Exception {
        when(bookingInterface.getBookingsBetweenDates(anyString(), anyString()))
                .thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(get("/between-dates")
                        .param("startDate", "2023-01-01")
                        .param("endDate", "2023-12-31"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("fail"))
                .andExpect(jsonPath("$.message").value("Database error"))
                .andDo(print());
    }

    @Test
    void testGetAllBookings_Exception() throws Exception {
        when(bookingInterface.getAllBookings())
                .thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(get("/bookings"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("fail"))
                .andExpect(jsonPath("$.message").value("Database error"))
                .andDo(print());
    }

    @Test
    void testGetBookingDetails_Exception() throws Exception {
        when(bookingInterface.getBookingDetails())
                .thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(get("/user-booking-details"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("fail"))
                .andExpect(jsonPath("$.message").value("Database error"))
                .andDo(print());
    }

    @Test
    void testCompleteBooking_Exception() throws Exception {
        when(bookingInterface.completeBooking(anyLong(), anyString()))
                .thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(put("/update-status/1/testuser"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("fail"))
                .andExpect(jsonPath("$.message").value("Database error"))
                .andDo(print());
    }

    @Test
    void testDeleteBooking_Exception() throws Exception {
        when(bookingInterface.cancelBooking(anyLong()))
                .thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(delete("/delete-booking/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("fail"))
                .andExpect(jsonPath("$.message").value("Database error"))
                .andDo(print());
    }

    @Test
    void testGetAllServiceTypes_Exception() throws Exception {
        when(serviceTypeInterface.getAllServiceTypes())
                .thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(get("/getallservices"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("fail"))
                .andExpect(jsonPath("$.message").value("Database error"))
                .andDo(print());
    }

    @Test
    void testGetTimings_Exception() throws Exception {
        when(bookingInterface.getTimings(anyLong(), anyString()))
                .thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(get("/timings")
                        .param("pincode", "560001")
                        .param("date", "2023-12-31"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("fail"))
                .andExpect(jsonPath("$.message").value("Database error"))
                .andDo(print());
    }



    @Test
    void testCompleteBooking() throws Exception {
        Map<String, Object> mockResponse = new HashMap<>();
        mockResponse.put("status", "success");

        when(bookingInterface.completeBooking(anyLong(), anyString()))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        mockMvc.perform(put("/update-status/1/testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andDo(print());
    }


    @Test
    void testDeleteBooking() throws Exception {
        Map<String, Object> mockResponse = new HashMap<>();
        mockResponse.put("status", "success");

        when(bookingInterface.cancelBooking(anyLong()))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        mockMvc.perform(delete("/delete-booking/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"));
    }

    @Test
    void testCountCompletedBookings() throws Exception {
        Map<String, Object> mockResponse = new HashMap<>();
        mockResponse.put("status", "success");
        mockResponse.put("count", 10);

        when(bookingInterface.countCompletedBookings(anyLong()))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        mockMvc.perform(get("/completed/count")
                        .param("pincode", "560001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.count").value(10));
    }

    @Test
    void testCountTotalCompletedBookings() throws Exception {
        Map<String, Object> mockResponse = new HashMap<>();
        mockResponse.put("status", "success");
        mockResponse.put("count", 100);

        when(bookingInterface.countTotalCompletedBookings())
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        mockMvc.perform(get("/bookings/count/completed"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.count").value(100));
    }


    @Test
    void testAddServiceType() throws Exception {
        ServiceType serviceType = new ServiceType();
        serviceType.setServiceName("SampleService");

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");

        ResponseEntity<Map<String, Object>> responseEntity = ResponseEntity.ok(response);
        when(serviceTypeInterface.addServiceType(any(ServiceType.class))).thenReturn((ResponseEntity) responseEntity);

        mockMvc.perform(post("/addservice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"serviceName\":\"SampleService\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andDo(print());

        // Additional test for exception handling
        when(serviceTypeInterface.addServiceType(any(ServiceType.class))).thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(post("/addservice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"serviceName\":\"SampleService\"}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("fail"))
                .andExpect(jsonPath("$.message").value("Database error"))
                .andDo(print());
    }



    @Test
    void testGetAvailableSlots() throws Exception {
        when(slotInterface.getAvailableSlots(anyLong(), anyString())).thenReturn(ResponseEntity.ok(3L));

        mockMvc.perform(get("/availableslots")
                        .param("pincode", "560001")
                        .param("date", "2023-12-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(3))
                .andDo(print());
    }

    @Test
    void testGetTimings() throws Exception {
        Map<String, Object> timings = new HashMap<>();
        timings.put("startTime", "09:00 AM");
        timings.put("endTime", "11:00 AM");

        ResponseEntity<Map<String, Object>> responseEntity = ResponseEntity.ok(timings);
        when(slotInterface.getTimings(560001L)).thenReturn(responseEntity);

        mockMvc.perform(get("/pincodetimings")
                        .param("pincode", "560001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.startTime").value("09:00 AM"))
                .andExpect(jsonPath("$.endTime").value("11:00 AM"))
                .andDo(print());

        // Additional test for exception handling
        when(slotInterface.getTimings(560001L)).thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(get("/pincodetimings")
                        .param("pincode", "560001"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("fail"))
                .andExpect(jsonPath("$.message").value("Database error"))
                .andDo(print());
    }

    @Test
    void testAddBooking() throws Exception {
        Booking booking = new Booking();
        booking.setUsername("testuser");
        booking.setPincode(560001L);
        booking.setDate("2023-12-31");
        booking.setStartTime("09:00 AM");
        booking.setStatus("Pending");

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");

        ResponseEntity<Map<String, Object>> responseEntity = ResponseEntity.ok(response);
        when(bookingInterface.addBooking(any(Booking.class), eq("testuser"))).thenReturn(responseEntity);

        mockMvc.perform(post("/bookslot/testuser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testuser\", \"pincode\":560001, \"date\":\"2023-12-31\", \"startTime\":\"09:00 AM\", \"status\":\"Pending\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andDo(print());

        // Additional test for exception handling
        when(bookingInterface.addBooking(any(Booking.class), eq("testuser"))).thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(post("/bookslot/testuser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testuser\", \"pincode\":560001, \"date\":\"2023-12-31\", \"startTime\":\"09:00 AM\", \"status\":\"Pending\"}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("fail"))
                .andExpect(jsonPath("$.message").value("Database error"))
                .andDo(print());
    }

    @Test
    void testGetBookingCount() throws Exception {
        Map<String, Object> countResponse = new HashMap<>();
        countResponse.put("count", 5);

        ResponseEntity<Map<String, Object>> responseEntity = ResponseEntity.ok(countResponse);
        when(bookingInterface.getBookingCount(560001L)).thenReturn(responseEntity);

        mockMvc.perform(get("/countbookings/560001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(5))
                .andDo(print());

        // Additional test for exception handling
        when(bookingInterface.getBookingCount(560001L)).thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(get("/countbookings/560001"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("fail"))
                .andExpect(jsonPath("$.message").value("Database error"))
                .andDo(print());
    }



@Test
    void testUpdateSlotTiming() throws Exception {
        UpdateSlotTimingRequest request = new UpdateSlotTimingRequest();
        request.setStartTime("10:00 AM");
        request.setEndTime("12:00 PM");

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");

        ResponseEntity<Map<String, Object>> responseEntity = ResponseEntity.ok(response);
        when(slotInterface.updateSlotTiming(eq(1L), any(UpdateSlotTimingRequest.class))).thenReturn(responseEntity);

        mockMvc.perform(put("/updatetime/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"startTime\":\"10:00 AM\", \"endTime\":\"12:00 PM\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andDo(print());

        // Additional test for exception handling
        when(slotInterface.updateSlotTiming(eq(1L), any(UpdateSlotTimingRequest.class))).thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(put("/updatetime/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"startTime\":\"10:00 AM\", \"endTime\":\"12:00 PM\"}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("fail"))
                .andExpect(jsonPath("$.message").value("Database error"))
                .andDo(print());
    }



}
