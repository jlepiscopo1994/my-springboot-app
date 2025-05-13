package com.example.backend.dao;

import com.example.backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // Custom query methods can be defined here if needed
    // For example, findByName(String name) or findByCategory(String category)
}
