package com.customerproduct.customerproductapi.controller;

import com.customerproduct.customerproductapi.dto.ApiResponse;
import com.customerproduct.customerproductapi.dto.CustomerDTO;
import com.customerproduct.customerproductapi.exception.CustomerServiceException;
import com.customerproduct.customerproductapi.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;
    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CustomerDTO>> createCustomer(@RequestBody CustomerDTO customerDTO) {
        try {
            logger.info("Received request to create customer: {}", customerDTO);
            CustomerDTO createdCustomer = customerService.createCustomer(customerDTO);
            logger.info("Customer created successfully: {}", createdCustomer);
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(createdCustomer));
        } catch (CustomerServiceException e) {
            logger.error("Error creating customer", e);
            return ResponseEntity.status(e.getHttpStatus()).body(ApiResponse.error(e.getUserMessage(), e.getDeveloperMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CustomerDTO>>> getAllCustomers() {
        logger.info("Received request to get all customers");
        List<CustomerDTO> customers = customerService.getAllCustomers();
        logger.info("Fetched all customers successfully: {}", customers);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(customers));
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<ApiResponse<CustomerDTO>> getCustomerById(@PathVariable Long customerId) {
        logger.info("Received request to get customer by ID: {}", customerId);
        CustomerDTO customer = customerService.getCustomerById(customerId);
        if (customer != null) {
            logger.info("Fetched customer by ID successfully: {}", customer);
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(customer));
        } else {
            logger.warn("Customer not found by ID: {}", customerId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("Customer not found", null));
        }
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<ApiResponse<CustomerDTO>> updateCustomer(@PathVariable Long customerId,
                                                                   @RequestBody CustomerDTO updatedCustomerDTO) {
        try {
            logger.info("Received request to update customer with ID: {}", customerId);
            CustomerDTO updated = customerService.updateCustomer(customerId, updatedCustomerDTO);

            if (updated != null) {
                logger.info("Customer updated successfully: {}", updated);
                return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(updated));
            } else {
                logger.warn("Customer not found by ID: {}", customerId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("Customer not found", null));
            }
        } catch (CustomerServiceException e) {
            logger.error("Error updating customer", e);
            return ResponseEntity.status(e.getHttpStatus()).body(ApiResponse.error(e.getUserMessage(), e.getDeveloperMessage()));
        }
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long customerId) {
        try {
            logger.info("Received request to delete customer with ID: {}", customerId);
            customerService.deleteCustomer(customerId);
            logger.info("Customer deleted successfully");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (CustomerServiceException e) {
            logger.error("Error deleting customer", e);
            return ResponseEntity.status(e.getHttpStatus()).build();
        }
    }
}
