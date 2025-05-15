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

/**
 * The `updateProduct` function updates an existing product with the provided ID using the
 * information from the updated product object after validating the name and price.
 * 
 * @param id The `id` parameter is the unique identifier of the product that needs to be updated. It is
 * used to retrieve the existing product from the repository based on this identifier.
 * @param product The `product` parameter is an instance of the `Product` class that contains the
 * updated information for a product. It typically includes the new name and price for the product that
 * needs to be updated in the system.
 * @return The `updateProduct` method returns the updated `Product` object after updating its name and
 * price in the database.
 */
    public Product updateProduct(Integer id, Product product) {
        if (product.getName() == null || product.getName().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }

        if (product.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Product price cannot be negative");
        }

        Product existingProduct = productRepository.findById((long) id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        existingProduct.setName(product.getName());
        existingProduct.setPrice(product.getPrice());

        return productRepository.save(existingProduct);
    }

/**
 * The `deleteProduct` function deletes a product by its ID after checking if it exists in the
 * repository.
 * 
 * @param id The `id` parameter in the `deleteProduct` method is of type `Long` and represents the
 * unique identifier of the product that needs to be deleted from the repository.
 */
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new IllegalArgumentException("Product not found");
        }
        productRepository.deleteById(id);
    }
}
