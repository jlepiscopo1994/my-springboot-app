package com.example.backend.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ProductRequestDTO {
    @NotBlank(message = "Product name is required")
    @Size(max = 255, message = "Product name must be less than or equal to 255 characters")
    private String name;

    @NotNull(message = "Product price is required")
    @DecimalMin(value = "0.00", inclusive = true, message = "Product price must be non-negative")
    private BigDecimal price;

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
