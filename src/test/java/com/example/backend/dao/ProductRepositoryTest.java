package com.example.backend.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.backend.entity.Product;

@ExtendWith(MockitoExtension.class)
class ProductRepositoryTest {

    @Mock
    private ProductRepository productRepository;

    /**
     * Test findById when the ID is found in the repository.
     * @see ProductRepository#findById(Long)
     */
    @Test
    void testFindById() {
        // Arrange
        final Long id = 1L;
        final Product product = new Product();
        // ID is auto-generated, no need to set it explicitly
        product.setName("Product1");
        product.setPrice(new BigDecimal("100.00"));

        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        // Act
        final Optional<Product> actual = productRepository.findById(id);

        // Assert
        final Optional<Product> expected = Optional.of(product);
        assertEquals(expected, actual);
        verify(productRepository).findById(id);
    }

    /**
     * Test findById when the ID is not found in the repository.
     * @see ProductRepository#findById(Long)
     */
    @Test
    void testFindByIdNotFound() {
        // Arrange
        final Long id = 1L;

        when(productRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        final Optional<Product> actual = productRepository.findById(id);

        // Assert
        final Optional<Product> expected = Optional.empty();
        assertEquals(expected, actual);
        verify(productRepository).findById(id);
    }

    @Test
    void testSave() {
        // Arrange
        final Product product = new Product();
        product.setName("Product1");
        product.setPrice(new BigDecimal("100.00"));

        // Act
        productRepository.save(product);

        // Assert
        verify(productRepository).save(product);
    }

    @Test
    void testSaveNull() {
        // Arrange
        final Product product = null;

        // Arrange
        when(productRepository.save(null)).thenThrow(new IllegalArgumentException("Product cannot be null"));

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> productRepository.save(product));
    }
}   