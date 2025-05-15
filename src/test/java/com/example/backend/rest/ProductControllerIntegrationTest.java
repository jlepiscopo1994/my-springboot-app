package com.example.backend.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.backend.entity.Product;
import com.example.backend.dao.ProductRepository;
import com.example.backend.dto.ProductRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PreDestroy;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import javax.print.attribute.standard.Media;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(properties = "spring.profiles.active=test")
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Testcontainers
public class ProductControllerIntegrationTest {

    @Container
    public static MySQLContainer<?> mysqlContainer;

    static {
        mysqlContainer = new MySQLContainer<>("mysql:8.0")
                .withDatabaseName("testdb")
                .withUsername("user")
                .withPassword("password");
        mysqlContainer.start();
    }

    @PreDestroy
    public void stopContainer() {
        if (mysqlContainer != null) {
            mysqlContainer.stop();
        }
    }
    
    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);
    }

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
    ProductRequestDTO request = new ProductRequestDTO();
    request.setName("Integration Book");
    request.setPrice(new BigDecimal("15.99"));

    mockMvc.perform(post("/api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").isNumber())
        .andExpect(jsonPath("$.name").value("Integration Book"))
        .andExpect(jsonPath("$.price").value(15.99));
    }

    @Test
    void createProduct_withBlankName_shouldReturn400() throws Exception {
        ProductRequestDTO request = new ProductRequestDTO();
        request.setName(""); // Invalid
        request.setPrice(new BigDecimal("10.00"));

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void createProduct_withNegativePrice_shouldReturn400() throws Exception {
        ProductRequestDTO request = new ProductRequestDTO();
        request.setName("Integration Book");
        request.setPrice(new BigDecimal("-10.00")); // Invalid

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }


    @Test
    void getAllProducts_shouldReturnList() throws Exception {
        Product entity1 = productRepository.save(new Product("Pen", new BigDecimal("2.00")));
        Product entity2 = productRepository.save(new Product("Pencil", new BigDecimal("1.00")));

        mockMvc.perform(get("/api/products"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$[0].id").value(entity1.getId()))
            .andExpect(jsonPath("$[0].name").value("Pen"))
            .andExpect(jsonPath("$[0].price").value(2.00))
            .andExpect(jsonPath("$[1].id").value(entity2.getId()))
            .andExpect(jsonPath("$[1].name").value("Pencil"))
            .andExpect(jsonPath("$[1].price").value(1.00));

    }

    @Test
    void getProductById_shouldReturnProductResponseDTO() throws Exception {
        Product entity = productRepository.save(new Product("Pen", new BigDecimal("2.00")));

        mockMvc.perform(get("/api/products/" + entity.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(entity.getId()))
            .andExpect(jsonPath("$.name").value("Pen"))
            .andExpect(jsonPath("$.price").value(2.00));
    }


    @Test
    void updateProduct_shouldReturn200AndUpdatedProduct() throws Exception {
    // Create and save initial product (setup)
        Product entity = productRepository.save(new Product("Old", new BigDecimal("1.00")));

        // Prepare DTO for update
        ProductRequestDTO update = new ProductRequestDTO();
        update.setName("Updated");
        update.setPrice(new BigDecimal("5.00"));

        mockMvc.perform(put("/api/products/" + entity.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(update)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(entity.getId()))
            .andExpect(jsonPath("$.name").value("Updated"))
            .andExpect(jsonPath("$.price").value(5.00));
    }


    @Test
    void putProduct_withBlankName_shouldReturn400() throws Exception {
        ProductRequestDTO productRequestDTO = new ProductRequestDTO();
        productRequestDTO.setName("");
        productRequestDTO.setPrice(new BigDecimal("29.99"));

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void putProduct_withNegativePrice_shouldReturn400() throws Exception {
        ProductRequestDTO productRequestDTO = new ProductRequestDTO();
        productRequestDTO.setName("Integration Book");
        productRequestDTO.setPrice(new BigDecimal("-29.99"));

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequestDTO)))
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
