package com.customerproduct.customerproductapi.service;

import com.customerproduct.customerproductapi.dto.ProductDTO;
import com.customerproduct.customerproductapi.entity.Product;
import com.customerproduct.customerproductapi.exception.ProductServiceException;
import com.customerproduct.customerproductapi.repository.ProductRepository;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);


    @Autowired
    public ProductService(ProductRepository productRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }


    public ProductDTO createProduct(ProductDTO productDTO) {
        try {
            logger.info("Creating product: {}", productDTO);
            Product product = modelMapper.map(productDTO, Product.class);
            Product createdProduct = productRepository.save(product);
            logger.info("Product created successfully: {}", createdProduct);
            productDTO.setId(createdProduct.getId());
            return modelMapper.map(createdProduct, ProductDTO.class);
        } catch (Exception e) {
            logger.error("Error creating product", e);
            throw e;
        }
    }

    public List<ProductDTO> getAllProducts() {
        logger.info("Fetching all products");
        List<Product> products = (List<Product>) productRepository.findAll();
        return products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());
    }

    @Cacheable(value = "products", key = "#productId")
    public ProductDTO getProductById(Long productId) {
        logger.info("Fetching product by ID: {}", productId);
        Product product = productRepository.findById(productId).orElse(null);
        return (product != null) ? modelMapper.map(product, ProductDTO.class) : null;
    }

    @CacheEvict(value = "products", key = "#productId")

    public ProductDTO updateProduct(Long productId, ProductDTO updatedProductDTO) {
        try {
            logger.info("Updating product with ID: {}", productId);
            Product existingProduct = productRepository.findById(productId).orElse(null);

            if (existingProduct != null) {
                // Exclude the ID from mapping
                modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
                modelMapper.map(updatedProductDTO, existingProduct);

                Product updatedProduct = productRepository.save(existingProduct);
                logger.info("Product updated successfully: {}", updatedProduct);
                return modelMapper.map(updatedProduct, ProductDTO.class);
            } else {
                throw ProductServiceException.builder()
                        .httpStatus(HttpStatus.NOT_FOUND)
                        .developerMessage("Product not found with ID: " + productId)
                        .userMessage("Product not found")
                        .build();
            }
        } catch (Exception e) {
            logger.error("Error updating product", e);
            throw e;
        }
    }

    @Transactional
    public void deleteProduct(Long productId) {
        try {
            logger.info("Deleting product with ID: {}", productId);
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> ProductServiceException.builder()
                            .httpStatus(HttpStatus.NOT_FOUND)
                            .developerMessage("Product not found with ID: " + productId)
                            .userMessage("Product not found")
                            .build());

            logger.info("Deleting product: {}", product);
            productRepository.deleteById(productId);
            logger.info("Product deleted successfully.");
        } catch (Exception e) {
            logger.error("Error deleting product", e);
            throw e;
        }
    }


}
