package com.happiest.bookingservice.service;

import com.happiest.bookingservice.dao.ProductInterface;
import com.happiest.bookingservice.dto.Product;

import com.happiest.bookingservice.exception.NoProductsFoundException;
import com.happiest.bookingservice.exception.ProductAlreadyAddedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    ProductInterface productInterface;

    private static final Logger LOGGER = LogManager.getLogger(ProductService.class);

    public Product addProduct(Product product) {
        try {
            LOGGER.info("Registering product with id: " + product.getProductid());

            Optional<Product> existingProduct = productInterface.findById(product.getProductid());

            if (existingProduct.isPresent()) {

                LOGGER.error("Product with ProductName " + product.getProductname() + " already exists");
                throw new ProductAlreadyAddedException();
            } else {
                LOGGER.info("Product with ProductId" + product.getProductid() + " registered successfully.");
                System.out.println(product);
                return productInterface.save(product);
            }

        } catch (ProductAlreadyAddedException e) {
            throw e;
        }
    }

    public List<Product> getAllProducts() {
        try {
            LOGGER.info("Fetching all products.");
            List<Product> products = productInterface.findAll();

            // Check if the product list is empty and throw an exception if it is
            if (products.isEmpty()) {
                throw new NoProductsFoundException("No products found.");
            }

            return products;
        } catch (NoProductsFoundException e) {
            // Log the specific exception
            LOGGER.error("No products found: " + e.getMessage());
            throw e; // Propagate the custom exception
        } catch (Exception e) {
            LOGGER.error("Error while fetching products: " + e.getMessage());
            throw new RuntimeException("Could not retrieve products at this time. Please try again later.");
        }
    }



}
