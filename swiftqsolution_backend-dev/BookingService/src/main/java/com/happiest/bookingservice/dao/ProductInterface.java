package com.happiest.bookingservice.dao;

import com.happiest.bookingservice.dto.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductInterface extends JpaRepository<Product, Long> {

}
