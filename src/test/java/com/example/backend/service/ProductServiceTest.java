package com.example.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.backend.entity.Product;
import com.example.backend.dao.ProductRepository;

class ProductServiceTest {

    private ProductRepository productRepository;
    private ProductService productService;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        productService = new ProductService(productRepository);
    }

    @Test
    void createProduct_shouldSaveAndReturnProduct() {
        // Arrange
        String name = "Test Product";
        BigDecimal price = new BigDecimal("10.00");
        Product product = new Product(name, price);

        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Product createdProduct = productService.createProduct(name, price);

        // Assert
        assertNotNull(createdProduct, "The created product should not be null");
        assertEquals(name, createdProduct.getName(), "The product name should match");
        assertEquals(price, createdProduct.getPrice(), "The product price should match");
        verify(productRepository).save(product);
    }

    @Test
    void createProduct_shouldThrowExceptionForEmptyName() {
        // Arrange
        String name = "";
        BigDecimal price = new BigDecimal("100.00");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.createProduct(name, price);
        });
        assertEquals("Product name cannot be empty", exception.getMessage());
    }

    @Test
    void createProduct_shouldThrowExceptionForNegativePrice() {
        // Arrange
        String name = "Product1";
        BigDecimal price = new BigDecimal("-100.00");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.createProduct(name, price);
        });
        assertEquals("Product price cannot be negative", exception.getMessage());
    }

    @Test
    void getAllProducts_shouldReturnListOfProducts() {
        // Arrange
        Product product1 = new Product("Product1", new BigDecimal("100.00"));
        Product product2 = new Product("Product2", new BigDecimal("200.00"));
        List<Product> products = List.of(product1, product2);

        when(productRepository.findAll()).thenReturn(products);

        // Act
        List<Product> actualProducts = productService.getAllProducts(); 

        // Assert    
        assertEquals(products, actualProducts);
        verify(productRepository).findAll();
    }

    @Test
    void getProductById_shouldReturnProduct() {
        // Arrange
        Long id = 1L;
        Product product = new Product("Product1", new BigDecimal("100.00"));

        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        // Act
        Optional<Product> actualProduct = productService.getProductById(id);

        // Assert
        assertEquals(Optional.of(product), actualProduct);
        verify(productRepository).findById(id);
    }

    @Test
    void updateProduct_shouldUpdateAndReturnProduct() {
        // Arrange
        Long id = 1L;
        String name = "UpdatedProduct";
        BigDecimal price = new BigDecimal("150.00");
        Product existingProduct = new Product("OldProduct", new BigDecimal("100.00"));

        when(productRepository.findById(id)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(existingProduct)).thenReturn(existingProduct);

        // Act
        Product updatedProduct = productService.updateProduct(id, name, price);

        // Assert
        assertEquals(existingProduct, updatedProduct);
        verify(productRepository).save(existingProduct);
    }

    @Test
    void updateProduct_shouldThrowExceptionForNegativePrice() {
        Product existing = new Product("Old", BigDecimal.ONE);
        when(productRepository.findById(1L)).thenReturn(Optional.of(existing));

        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            productService.updateProduct(1L, "Updated", new BigDecimal("-100.00"));
        });
        assertEquals("Product price cannot be negative", e.getMessage());
        verify(productRepository, never()).save(any());
    }

    @Test
    void updateProduct_shouldThrowIfNotFound() {
        // Arrange
        Long id = 1L;
        String name = "UpdatedProduct";
        BigDecimal price = new BigDecimal("150.00");

        when(productRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.updateProduct(id, name, price);
        });
        assertEquals("Product not found", exception.getMessage());
    }

    @Test
    void deleteProduct_shouldDeleteIfExists() {
        // Arrange
        Long id = 1L;

        when(productRepository.existsById(id)).thenReturn(true);

        // Act
        productService.deleteProduct(id);

        // Assert
        verify(productRepository).deleteById(id);
    }

    @Test
    void deleteProduct_shouldThrowIfNotFound() {
        // Arrange
        Long id = 1L;

        when(productRepository.existsById(id)).thenReturn(false);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.deleteProduct(id);
        });
        assertEquals("Product not found", exception.getMessage());
    }


	
}
