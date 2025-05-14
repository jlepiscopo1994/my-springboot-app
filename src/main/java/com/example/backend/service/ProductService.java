package com.example.backend.service;

import org.springframework.stereotype.Service;

import com.example.backend.dao.ProductRepository;
import com.example.backend.entity.Product;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

/**
 * The `createProduct` function creates a new product with a given name and price, performing
 * validation checks before saving it to the repository.
 * 
 * @param name The `name` parameter is a `String` representing the name of the product being created.
 * @param price The `price` parameter in the `createProduct` method is of type `BigDecimal`. It is used
 * to specify the price of the product being created.
 * @return The `createProduct` method returns the `Product` object that is saved in the
 * `productRepository`.
 */
    public Product createProduct(String name, BigDecimal price) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Product price cannot be negative");
        }
        Product product = new Product(name, price);
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product updateProduct(Integer id, Product updatedProduct) {
        if (updatedProduct.getName() == null || updatedProduct.getName().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }

        if (updatedProduct.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Product price cannot be negative");
        }

        Product existingProduct = productRepository.findById((long) id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        
        existingProduct.setName(updatedProduct.getName());
        existingProduct.setPrice(updatedProduct.getPrice());

        return productRepository.save(existingProduct);
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new IllegalArgumentException("Product not found");
        }
        productRepository.deleteById(id);
    }
}
