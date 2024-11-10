package com.happiest.bookingservice.service;

import com.happiest.bookingservice.dao.ProductInterface;
import com.happiest.bookingservice.dto.Product;
import com.happiest.bookingservice.exception.NoProductsFoundException;
import com.happiest.bookingservice.exception.ProductAlreadyAddedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    @Mock
    private ProductInterface productInterface;

    @InjectMocks
    private ProductService productService;

    private Product product;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        product = new Product();
        product.setProductid(1L);
        product.setProductname("Test Product");
    }

    @Test
    public void testAddProductSuccess() {
        // Arrange
        when(productInterface.findById(product.getProductid())).thenReturn(Optional.empty());
        when(productInterface.save(product)).thenReturn(product);

        // Act
        Product result = productService.addProduct(product);

        // Assert
        assertNotNull(result);
        assertEquals("Test Product", result.getProductname());
        verify(productInterface, times(1)).save(product);
    }

//    @Test
//    public void testAddProductAlreadyExists() {
//        // Arrange
//        when(productInterface.findById(product.getProductid())).thenReturn(Optional.of(product));
//
//        // Act & Assert
//        assertThrows(ProductAlreadyAddedException.class, () -> productService.addProduct(product));
//        verify(productInterface, never()).save(any(Product.class));
//    }

    @Test
    public void testGetAllProductsSuccess() {
        // Arrange
        List<Product> products = new ArrayList<>();
        products.add(product);
        when(productInterface.findAll()).thenReturn(products);

        // Act
        List<Product> result = productService.getAllProducts();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Product", result.get(0).getProductname());
        verify(productInterface, times(1)).findAll();
    }

    @Test
    public void testGetAllProductsEmptyList() {
        // Arrange
        when(productInterface.findAll()).thenReturn(new ArrayList<>());

        // Act & Assert
        assertThrows(NoProductsFoundException.class, () -> productService.getAllProducts());
        verify(productInterface, times(1)).findAll();
    }

    @Test
    public void testGetAllProductsUnexpectedException() {
        // Arrange
        when(productInterface.findAll()).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> productService.getAllProducts());
        assertEquals("Could not retrieve products at this time. Please try again later.", exception.getMessage());
        verify(productInterface, times(1)).findAll();
    }
}

