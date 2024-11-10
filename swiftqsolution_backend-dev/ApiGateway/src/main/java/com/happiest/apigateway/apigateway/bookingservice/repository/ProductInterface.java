package com.happiest.apigateway.apigateway.bookingservice.repository;


import com.happiest.apigateway.apigateway.bookingservice.model.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="http://BookingService/product")
public interface ProductInterface {

    @PostMapping("/addproduct")
    public ResponseEntity<?> addProduct(@RequestBody Product product);

    @GetMapping("/getallproducts")
    public ResponseEntity<?> getAllProducts();

}
