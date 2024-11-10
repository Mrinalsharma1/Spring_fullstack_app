package com.happiest.bookingservice.controller;

import com.happiest.bookingservice.dto.Product;
import com.happiest.bookingservice.exception.NoProductsFoundException;
import com.happiest.bookingservice.exception.ProductAlreadyAddedException;
import com.happiest.bookingservice.service.ProductService;
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
class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    public ProductControllerTest() {
        MockitoAnnotations.initMocks(this);
    }

    // Test addProduct - success case
    @Test
    void addProduct_ShouldReturnCreatedStatus_WhenProductAddedSuccessfully() throws ProductAlreadyAddedException {
        Product product = new Product(1L, "Test Product");

        when(productService.addProduct(any(Product.class))).thenReturn(product);

        ResponseEntity<?> response = productController.addProduct(product);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(product, response.getBody());
        verify(productService, times(1)).addProduct(product);
    }

    // Test addProduct - product already added exception
    @Test
    void addProduct_ShouldReturnConflictStatus_WhenProductAlreadyAdded() throws ProductAlreadyAddedException {
        Product product = new Product(1L, "Test Product");

        doThrow(new ProductAlreadyAddedException("Product already added")).when(productService).addProduct(any(Product.class));

        ResponseEntity<?> response = productController.addProduct(product);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Error occured:", response.getBody());
        verify(productService, times(1)).addProduct(product);
    }

    // Test addProduct - internal server error
    @Test
    void addProduct_ShouldReturnInternalServerError_WhenUnexpectedErrorOccurs() throws ProductAlreadyAddedException {
        Product product = new Product(1L, "Test Product");

        doThrow(new RuntimeException("Unexpected error")).when(productService).addProduct(any(Product.class));

        ResponseEntity<?> response = productController.addProduct(product);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error occurred while adding product!", response.getBody());
        verify(productService, times(1)).addProduct(product);
    }

    // Test getAllProducts - success case
    @Test
    void getAllProducts_ShouldReturnOkStatus_WhenProductsExist() throws NoProductsFoundException {
        List<Product> products = Arrays.asList(
                new Product(1L, "Product 1"),
                new Product(2L, "Product 2")
        );

        when(productService.getAllProducts()).thenReturn(products);

        ResponseEntity<?> response = productController.getAllProducts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(products, response.getBody());
        verify(productService, times(1)).getAllProducts();
    }

    // Test getAllProducts - no products found exception
    @Test
    void getAllProducts_ShouldReturnNotFoundStatus_WhenNoProductsExist() throws NoProductsFoundException {
        doThrow(new NoProductsFoundException("No products found")).when(productService).getAllProducts();

        ResponseEntity<?> response = productController.getAllProducts();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Error occured: No products found", response.getBody());
        verify(productService, times(1)).getAllProducts();
    }

    // Test getAllProducts - internal server error
    @Test
    void getAllProducts_ShouldReturnInternalServerError_WhenUnexpectedErrorOccurs() throws NoProductsFoundException {
        doThrow(new RuntimeException("Unexpected error")).when(productService).getAllProducts();

        ResponseEntity<?> response = productController.getAllProducts();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error retrieving products: Unexpected error", response.getBody());
        verify(productService, times(1)).getAllProducts();
    }
}

