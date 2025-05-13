package com.example.backend.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import com.example.backend.dao.ProductRepository;
import com.example.backend.service.ProductService;
import com.example.backend.entity.Product;


@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:3000") // Adjust the origin as needed
public class ProductController {
    

    private final ProductRepository productRepository;

    private final ProductService productService;

    public ProductController(ProductRepository productRepository, ProductService productService) {
        this.productRepository = productRepository;
        this.productService = productService;
    }

/**
 * Retrieves a list of all products.
 *
 * @return a list of all products stored in the repository
 */
    @GetMapping
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Creates a new product.
     *
     * @param product the product to create
     * @return the created product
     */
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product created = productService.createProduct(product.getName(), product.getPrice());
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /**
     * Retrieves a product by its ID.
     *
     * @param id the ID of the product to retrieve
     * @return the product with the given ID, or null if no such product exists
     */
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productRepository.findById(id).orElse(null);
    }

    /**
     * Updates a product.
     *
     * @param id the ID of the product to update
     * @param updatedProduct the product with the updated information
     * @return the updated product, or a 404 if no product with the given ID exists
     */
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product updatedProduct) {
        Product product = productService.updateProduct(id, updatedProduct.getName(), updatedProduct.getPrice());
        return ResponseEntity.ok(product);
    }

    /**
     * Deletes a product.
     *
     * @param id the ID of the product to delete
     * @return a 204 status code if the product was successfully deleted, or a 404 if no product with the given ID exists
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        productService.deleteProduct((long) id);
        return ResponseEntity.noContent().build();
    }

    

}
