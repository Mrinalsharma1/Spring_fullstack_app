package com.happiest.bookingservice.controller;

import com.happiest.bookingservice.dto.Product;

import com.happiest.bookingservice.exception.NoProductsFoundException;
import com.happiest.bookingservice.exception.ProductAlreadyAddedException;
import com.happiest.bookingservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/product")
@RestController
@CrossOrigin
public class ProductController {
    @Autowired
    private ProductService productService;

    // Add product endpoint
    @PostMapping("/addproduct")
    public ResponseEntity<?> addProduct(@RequestBody Product product) {
        try {
            // Call the service layer to add a new product
            Product newProduct = productService.addProduct(product);

            // Return the newly added product in the response with a success message
            return new ResponseEntity<>(newProduct, HttpStatus.CREATED);

        } catch (ProductAlreadyAddedException e) {
            // If the product is already added, handle the exception and return an error response

            return new ResponseEntity<>("Error occured:", HttpStatus.CONFLICT);
        } catch (Exception e) {
            // Handle general exceptions

            return new ResponseEntity<>("Error occurred while adding product!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/getallproducts")
    public ResponseEntity<?> getAllProducts() {
        try {
            List<Product> products = productService.getAllProducts();
            return new ResponseEntity<>(products, HttpStatus.OK);
        } catch (NoProductsFoundException e) {
            // Return a response with HTTP 404 (Not Found)
            return new ResponseEntity<>("Error occured: " + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            // Log the error message and return a response with HTTP 500 (Internal Server Error)
            return new ResponseEntity<>("Error retrieving products: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
