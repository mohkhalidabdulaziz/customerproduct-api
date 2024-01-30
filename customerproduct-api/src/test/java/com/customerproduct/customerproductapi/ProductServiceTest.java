package com.customerproduct.customerproductapi;

import com.customerproduct.customerproductapi.dto.ProductDTO;
import com.customerproduct.customerproductapi.entity.Product;
import com.customerproduct.customerproductapi.exception.ProductServiceException;
import com.customerproduct.customerproductapi.repository.ProductRepository;
import com.customerproduct.customerproductapi.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateProduct() {
        ProductDTO requestDTO = createTestProductDTO();
        Product createdProduct = createTestProductEntity();

        when(modelMapper.map(requestDTO, Product.class)).thenReturn(createdProduct);
        when(productRepository.save(createdProduct)).thenReturn(createdProduct);
        when(modelMapper.map(createdProduct, ProductDTO.class)).thenReturn(requestDTO);

        ProductDTO result = productService.createProduct(requestDTO);

        assertNotNull(result);
        assertEquals(requestDTO, result);

        verify(productRepository, times(1)).save(createdProduct);
    }

    @Test
    void testGetAllProducts() {
        Product product = createTestProductEntity();
        when(productRepository.findAll()).thenReturn(Collections.singletonList(product));
        when(modelMapper.map(product, ProductDTO.class)).thenReturn(createTestProductDTO());

        assertEquals(1, productService.getAllProducts().size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testGetProductById() {
        Long productId = 1L;
        Product product = createTestProductEntity();
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(modelMapper.map(product, ProductDTO.class)).thenReturn(createTestProductDTO());

        ProductDTO result = productService.getProductById(productId);

        assertNotNull(result);
        assertEquals(createTestProductDTO(), result);

        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void testGetProductByIdNotFound() {
        Long productId = 1L;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertNull(productService.getProductById(productId));

        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void testUpdateProduct() {
        Long productId = 1L;
        ProductDTO updatedDTO = createTestProductDTO();
        Product existingProduct = createTestProductEntity();

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(modelMapper.map(updatedDTO, Product.class)).thenReturn(existingProduct);
        when(productRepository.save(existingProduct)).thenReturn(existingProduct);
        when(modelMapper.map(existingProduct, ProductDTO.class)).thenReturn(updatedDTO);

        ProductDTO result = productService.updateProduct(productId, updatedDTO);

        assertNotNull(result);
        assertEquals(updatedDTO, result);

        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).save(existingProduct);
    }

    @Test
    void testUpdateProductNotFound() {
        Long productId = 1L;
        ProductDTO updatedDTO = createTestProductDTO();

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ProductServiceException.class, () -> productService.updateProduct(productId, updatedDTO));

        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, never()).save(any());
    }

    @Test
    void testDeleteProduct() {
        Long productId = 1L;
        Product product = createTestProductEntity();

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        assertDoesNotThrow(() -> productService.deleteProduct(productId));

        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).deleteById(productId);
    }

    @Test
    void testDeleteProductNotFound() {
        Long productId = 1L;

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ProductServiceException.class, () -> productService.deleteProduct(productId));

        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, never()).deleteById(any());
    }

    private ProductDTO createTestProductDTO() {
        return ProductDTO.builder()
                .id(1L)
                .bookTitle("Test Book")
                .bookPrice(29.99)
                .bookQuantity(10)
                .build();
    }

    private Product createTestProductEntity() {
        Product product = new Product();
        product.setId(1L);
        product.setBookTitle("Test Book");
        product.setBookPrice(29.99);
        product.setBookQuantity(10);
        return product;
    }
}
