package com.example.backend.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doThrow;
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
import org.springframework.http.ResponseEntity;

import com.example.backend.entity.Product;
import com.example.backend.service.ProductService;
import com.example.backend.dao.ProductRepository;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductService productService;

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
    void testCreateProduct_returnsCreated() {
        // Arrange
        Product product = new Product("Product1", new BigDecimal("100.00"));
        when(productService.createProduct("Product1", new BigDecimal("100.00"))).thenReturn(product);

        // Act
        ResponseEntity<Product> response = productController.createProduct(product);

        // Assert
        assertEquals(201, response.getStatusCode().value());
        assertEquals(product, response.getBody());
    }

    @Test
    void testCreateProduct_returnsNegativePriceException() {
        // Arrange
        Product product = new Product("Product1", new BigDecimal("-100.00"));
        when(productService.createProduct("Product1", new BigDecimal("-100.00")))
                .thenThrow(new IllegalArgumentException("Product price cannot be negative"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            productController.createProduct(product);
        });
    }

    @Test
    void updateProduct_returnsUpdatedProduct() {
        // Arrange
        Product updatedProduct = new Product("UpdatedProduct", new BigDecimal("150.00"));
        Product updatedProductInput = new Product("UpdatedProduct", new BigDecimal("150.00"));
        when(productService.updateProduct(1, updatedProductInput))
                .thenReturn(updatedProduct);

        // Act
        ResponseEntity<Product> response = productController.updateProduct((int) 1L, updatedProduct);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals(updatedProduct, response.getBody());
    }


    @Test
    void updateProduct_returnsNotFound_whenProductMissing() {
        Product input = new Product("X", BigDecimal.ONE);
        when(productService.updateProduct(1, input))
                .thenThrow(new IllegalArgumentException("Product not found"));
        // This will now raise the exception, handled globally in real app.
        // In direct calls, you may need to use @WebMvcTest and MockMvc to test full path.
        assertThrows(IllegalArgumentException.class, () -> {
            productController.updateProduct(1, input);
        });
    }

    @Test
    void deleteProduct_returnsNotFound_whenMissing() {
        doThrow(new IllegalArgumentException("Product not found")).when(productService).deleteProduct(1L);

        assertThrows(IllegalArgumentException.class, () -> {
            productController.deleteProduct(1);
        });
    }

}

