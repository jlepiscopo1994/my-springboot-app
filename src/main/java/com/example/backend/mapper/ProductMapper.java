package com.example.backend.mapper;

import com.example.backend.dto.ProductRequestDTO;
import com.example.backend.dto.ProductResponseDTO;
import com.example.backend.entity.Product;

public class ProductMapper {

    
    /**
     * Maps a ProductRequestDTO to a Product entity. If the DTO is null, then null is returned.
     * 
     * @param dto The ProductRequestDTO to be mapped
     * @return The mapped Product entity
     */
    public static Product toEntity(ProductRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        Product product = new Product();
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        return product;
    }


    /**
     * Maps a Product entity to a ProductResponseDTO. If the entity is null, then null is returned.
     * 
     * @param product The Product entity to be mapped
     * @return The mapped ProductResponseDTO
     */
    public static ProductResponseDTO toDTO(Product product) {
        if (product == null) {
            return null;
        }
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        return dto;
    }
}
