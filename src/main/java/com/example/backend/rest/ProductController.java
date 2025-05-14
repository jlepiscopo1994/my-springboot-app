package com.example.backend.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import com.example.backend.dao.ProductRepository;
import com.example.backend.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;

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
 * This Java function uses Spring's @GetMapping annotation to retrieve all products from a repository
 * and return them as a list.
 * 
 * @return A list of all products from the product repository is being returned.
 */
    @GetMapping
    @Operation(summary = "Get all products")
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
/**
 * This Java function creates a new product using the provided name and price, returning the created
 * product in a ResponseEntity with HTTP status code 201 (Created).
 * 
 * @param product The `product` parameter in the `createProduct` method is of type `Product` and is
 * annotated with `@RequestBody`. This means that the method expects a JSON representation of a
 * `Product` object in the request body. The `@Valid` annotation is used to trigger validation of the `
 * @return The method `createProduct` is returning a `ResponseEntity` object with the created `Product`
 * and a status of `HttpStatus.CREATED`.
 */

    @PostMapping
    @Operation(summary = "Create a new product")
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product) {
        Product created = productService.createProduct(product.getName(), product.getPrice());
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

/**
 * This Java function retrieves a product by its ID from a repository.
 * 
 * @param id The `id` parameter in the `getProductById` method is a `Long` type variable representing
 * the unique identifier of the product that the method is trying to retrieve from the database.
 * @return The `getProductById` method is returning a `Product` object with the specified `id` from the
 * `productRepository`. If no product is found with the given `id`, it will return `null`.
 */
    @GetMapping("/{id}")
    @Operation(summary = "Get a product by ID")
    public Product getProductById(@Parameter(description = "The ID of the product to retrieve") @PathVariable Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a product by ID")
    public ResponseEntity<Product> updateProduct(
        @Parameter(description = "The ID of the product to update") @PathVariable Integer id,
        @Valid @RequestBody Product updatedProduct
    ) {
        Product product = productService.updateProduct(id, updatedProduct);
        return ResponseEntity.ok(product);
    }

/**
 * This Java function deletes a product by its ID and returns a response entity with no content.
 * 
 * @param id The `id` parameter in the `deleteProduct` method is used to specify the identifier of the
 * product that needs to be deleted. It is extracted from the path variable in the URL when a DELETE
 * request is made to the endpoint mapped to this method.
 * @return The method is returning a `ResponseEntity` with a status of `204 No Content`.
 */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a product by ID")
    public ResponseEntity<Void> deleteProduct(@Parameter(description = "The ID of the product to delete") @PathVariable Integer id) {
        productService.deleteProduct((long) id);
        return ResponseEntity.noContent().build();
    }

    

}
