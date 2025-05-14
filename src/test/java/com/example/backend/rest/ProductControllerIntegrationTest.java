package com.example.backend.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.backend.entity.Product;
import com.example.backend.dao.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        // Clear the repository before each test
        productRepository.deleteAll();
    }

    @Test
    void createProduct_shouldReturn201AndProduct() throws Exception {
        Product product = new Product("Integration Book", new BigDecimal("29.99"));

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.price").value("29.99"));
    }

    @Test
    void getAllProducts_shouldReturnList() throws Exception {
        productRepository.save(new Product("Integration Book", new BigDecimal("29.99")));
        productRepository.save(new Product("Integration Book 2", new BigDecimal("39.99")));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void putProduct_withBlankName_shouldReturn400() throws Exception {
        Product product = new Product("", new BigDecimal("29.99"));

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void putProduct_withNegativePrice_shouldReturn400() throws Exception {
        Product product = new Product("Integration Book", new BigDecimal("-29.99"));

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteProduct_shouldReturn204() throws Exception {
        Product product = productRepository.save(new Product("Integration Book", new BigDecimal("29.99")));

        mockMvc.perform(delete("/api/products/" + product.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteProduct_whenNotFound_shouldReturn400() throws Exception {
        mockMvc.perform(delete("/api/products/999"))
                .andExpect(status().isBadRequest());
    }

}