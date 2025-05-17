package com.example.backend.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.backend.service.ProductService;

import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;

import com.example.backend.dto.ProductRequestDTO;
import com.example.backend.dto.ProductResponseDTO;
import com.example.backend.mapper.ProductMapper;
import com.example.backend.entity.Product;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:3000") // Adjust the origin as needed
public class ProductController {

    private static final Logger logger = LogManager.getLogger(ProductController.class);
    

    private final ProductService productService;

    private final MeterRegistry meterRegistry;
    

    public ProductController(ProductService productService, MeterRegistry meterRegistry) {
        this.productService = productService;
        this.meterRegistry = meterRegistry;
    }


/**
 * This Java function retrieves all products from a service, maps them to DTOs, and returns them as a
 * JSON response.
 * 
 * @return A ResponseEntity containing a list of ProductResponseDTO objects is being returned.
 */
    @GetMapping
    @Operation(summary = "Get all products")
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        meterRegistry.counter("products.getAll").increment();
        logger.info("Calling endpoint to get all products");
        List<Product> products = productService.getAllProducts();
        List<ProductResponseDTO> productDTOs = products.stream()
                .map(ProductMapper::toDTO)
                .collect(Collectors.toList());

        // Return the list of products as a JSON response
        return ResponseEntity.ok(productDTOs);
    }


/**
 * This function creates a new product based on the provided request data and returns a response with
 * the created product details.
 * 
 * @param dto The `dto` parameter in the `createProduct` method is of type `ProductRequestDTO`. It is
 * annotated with `@Valid` to indicate that the input should be validated before processing. The
 * `@RequestBody` annotation is used to bind the HTTP request body to the `dto` parameter.
 * @return The method `createProduct` is returning a `ResponseEntity` object containing a
 * `ProductResponseDTO` with the newly created product information, along with an HTTP status code of
 * 201 (HttpStatus.CREATED).
 */
    @PostMapping
    @Operation(summary = "Create a new product")
    public ResponseEntity<ProductResponseDTO> createProduct(@Valid @RequestBody ProductRequestDTO dto) {
        meterRegistry.counter("products.create").increment();
        logger.info("Calling endpoint to create a new product");
        Product created = productService.createProduct(ProductMapper.toEntity(dto).getName(), ProductMapper.toEntity(dto).getPrice());
        return new ResponseEntity<>(ProductMapper.toDTO(created), HttpStatus.CREATED);
    }


/**
 * This function retrieves a product by its ID and returns a ResponseEntity containing the product
 * information in a DTO format.
 * 
 * @param id The `id` parameter in the `getProductById` method is a path variable representing the ID
 * of the product to retrieve. It is annotated with `@PathVariable` to indicate that the value for this
 * parameter will be extracted from the URI path of the request URL.
 * @return The method is returning a `ResponseEntity` containing a `ProductResponseDTO` object.
 */
    @GetMapping("/{id}")
    @Operation(summary = "Get a product by ID")
    public ResponseEntity<ProductResponseDTO> getProductById(@Parameter(description = "The ID of the product to retrieve") @PathVariable Long id) {
        meterRegistry.counter("products.getById").increment();
        logger.info("Calling endpoint to get product by ID: " + id);
        Optional<Product> product = productService.getProductById(id);
        if (product.isEmpty()) {
            logger.error("Error found in getProductById with ID: " + id + " - Product not found");
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ProductMapper.toDTO(product.get()));
    }

/**
 * This Java function updates a product by ID and returns the updated product.
 * 
 * @param id The `id` parameter in the `updateProduct` method is used to specify the ID of the product
 * that needs to be updated. This ID is extracted from the path variable in the URL mapping
 * `@PutMapping("/{id}")`.
 * @param updatedProduct The `updatedProduct` parameter in the `updateProduct` method is of type
 * `Product` and represents the product object with updated information that will be used to update the
 * existing product in the system. This parameter is annotated with `@RequestBody` and `@Valid`,
 * indicating that the data for the
 * @return The `updateProduct` method is returning a `ResponseEntity` object with the updated `Product`
 * entity inside it.
 */
    @PutMapping("/{id}")
    @Operation(summary = "Update a product by ID")
    public ResponseEntity<Product> updateProduct(
        @Parameter(description = "The ID of the product to update") @PathVariable Integer id,
        @Valid @RequestBody Product updatedProduct
    ) {
        meterRegistry.counter("products.update").increment();
        logger.info("Calling endpoint to update product by ID: " + id);
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
        meterRegistry.counter("products.delete").increment();
        logger.info("Calling endpoint to delete product by ID: " + id);
        productService.deleteProduct((long) id);
        return ResponseEntity.noContent().build();
    }

    

}
