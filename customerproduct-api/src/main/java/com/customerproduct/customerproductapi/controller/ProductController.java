package com.customerproduct.customerproductapi.controller;

import com.customerproduct.customerproductapi.dto.ApiResponse;
import com.customerproduct.customerproductapi.dto.ProductDTO;
import com.customerproduct.customerproductapi.exception.ProductServiceException;
import com.customerproduct.customerproductapi.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductDTO>> createProduct(@RequestBody ProductDTO productDTO) {
        try {
            logger.info("Received request to create product: {}", productDTO);
            ProductDTO createdProduct = productService.createProduct(productDTO);
            logger.info("Product created successfully: {}", createdProduct);
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(createdProduct));
        } catch (ProductServiceException e) {
            logger.error("Error creating product", e);
            return ResponseEntity.status(e.getHttpStatus()).body(ApiResponse.error(e.getUserMessage(), e.getDeveloperMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getAllProducts() {
        logger.info("Received request to get all products");
        List<ProductDTO> products = productService.getAllProducts();
        logger.info("Fetched all products successfully: {}", products);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(products));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductDTO>> getProductById(@PathVariable Long productId) {
        logger.info("Received request to get product by ID: {}", productId);
        ProductDTO product = productService.getProductById(productId);
        if (product != null) {
            logger.info("Fetched product by ID successfully: {}", product);
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(product));
        } else {
            logger.warn("Product not found by ID: {}", productId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("Product not found", null));
        }
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductDTO>> updateProduct(@PathVariable Long productId,
                                                                 @RequestBody ProductDTO updatedProductDTO) {
        try {
            logger.info("Received request to update product with ID: {}", productId);
            ProductDTO updated = productService.updateProduct(productId, updatedProductDTO);

            if (updated != null) {
                logger.info("Product updated successfully: {}", updated);
                return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(updated));
            } else {
                logger.warn("Product not found by ID: {}", productId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("Product not found", null));
            }
        } catch (ProductServiceException e) {
            logger.error("Error updating product", e);
            return ResponseEntity.status(e.getHttpStatus()).body(ApiResponse.error(e.getUserMessage(), e.getDeveloperMessage()));
        }
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        try {
            logger.info("Received request to delete product with ID: {}", productId);
            productService.deleteProduct(productId);
            logger.info("Product deleted successfully");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (ProductServiceException e) {
            logger.error("Error deleting product", e);
            return ResponseEntity.status(e.getHttpStatus()).build();
        }
    }
}

