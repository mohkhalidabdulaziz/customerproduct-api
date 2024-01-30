package com.customerproduct.customerproductapi;

import com.customerproduct.customerproductapi.controller.ProductController;
import com.customerproduct.customerproductapi.dto.ProductDTO;
import com.customerproduct.customerproductapi.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.Mockito.*;

class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testCreateProduct() throws Exception {
        ProductDTO requestDTO = createTestProductDTO();
        when(productService.createProduct(any())).thenReturn(createTestProductDTO());

        mockMvc.perform(MockMvcRequestBuilders.post("/customerproduct-api/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(1));

        verify(productService, times(1)).createProduct(any());
    }

    @Test
    void testGetAllProducts() throws Exception {
        when(productService.getAllProducts()).thenReturn(Collections.singletonList(createTestProductDTO()));

        mockMvc.perform(MockMvcRequestBuilders.get("/customerproduct-api/api/products"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").value(1));

        verify(productService, times(1)).getAllProducts();
    }

    @Test
    void testGetProductById() throws Exception {
        Long productId = 1L;

        // Mock the service response
        ProductDTO mockResponse = createTestProductDTO();
        when(productService.getProductById(productId)).thenReturn(mockResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/customerproduct-api/api/products/{productId}", productId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.bookTitle").value("Test Book"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.bookPrice").value(29.99))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.bookQuantity").value(10));

        // Verify that the service method was called
        verify(productService, times(1)).getProductById(productId);
    }

    @Test
    void testUpdateProduct() throws Exception {
        Long productId = 1L;
        ProductDTO updatedDTO = createTestProductDTO();
        when(productService.updateProduct(eq(productId), any())).thenReturn(createTestProductDTO());

        mockMvc.perform(MockMvcRequestBuilders.put("/customerproduct-api/api/products/{productId}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(1));

        verify(productService, times(1)).updateProduct(eq(productId), any());
    }

    @Test
    void testDeleteProduct() throws Exception {
        Long productId = 1L;
        mockMvc.perform(MockMvcRequestBuilders.delete("/customerproduct-api/api/products/{productId}", productId))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(productService, times(1)).deleteProduct(productId);
    }

    private ProductDTO createTestProductDTO() {
        return ProductDTO.builder()
                .id(1L)
                .bookTitle("Test Book")
                .bookPrice(29.99)
                .bookQuantity(10)
                .build();
    }
}
