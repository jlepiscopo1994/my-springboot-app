package com.example.backend.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.backend.entity.Product;
import com.example.backend.dao.ProductRepository;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductController productController;

    @Test
    void testGetAllProducts() {
        // Arrange
        Product product1 = new Product("Product1", new BigDecimal("100.00"));
        Product product2 = new Product("Product2", new BigDecimal("200.00"));
        List<Product> products = List.of(product1, product2);

        when(productRepository.findAll()).thenReturn(products);

        // Act
        List<Product> actualProducts = productController.getAllProducts();

        // Assert
        assertEquals(products, actualProducts);
        verify(productRepository).findAll();
    }
    
    @Test
    void testCreateProduct() {
        // Arrange
        Product product = new Product("Product3", new BigDecimal("150.00"));

        when(productRepository.save(product)).thenReturn(product);

        // Act
        Product createdProduct = productController.createProduct(product);

        // Assert
        assertEquals(product, createdProduct);
        verify(productRepository).save(product);
    }
}

