package com.customerproduct.customerproductapi.service;

import com.customerproduct.customerproductapi.dto.CustomerDTO;
import com.customerproduct.customerproductapi.entity.Customer;
import com.customerproduct.customerproductapi.exception.CustomerServiceException;
import com.customerproduct.customerproductapi.repository.CustomerRepository;
import com.customerproduct.customerproductapi.utils.MappingUtils;
import lombok.AllArgsConstructor;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;
    private final MappingUtils mappingUtils;

    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        try {
            logger.info("Validating email does not exist: {}", customerDTO.getEmail());
            mappingUtils.validateEmailNotExists(customerDTO.getEmail());

            Customer customer = mappingUtils.convertToEntity(customerDTO);

            // Set the parent for each family member
            if (customer.getFamilyMembers() != null) {
                customer.getFamilyMembers().forEach(familyMember -> familyMember.setParent(customer));
            }

            Customer createdCustomer = customerRepository.save(customer);

            logger.info("Customer created successfully: {}", createdCustomer);
            customerDTO.setId(createdCustomer.getId());
            return mappingUtils.convertToDTO(createdCustomer);
        } catch (Exception e) {
            logger.error("Error creating customer", e);
            throw e;
        }
    }

    public List<CustomerDTO> getAllCustomers() {
        logger.info("Fetching all customers");
        List<Customer> customers = (List<Customer>) customerRepository.findAll();
        return customers.stream()
                .map(mappingUtils::convertToDTO)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "customers", key = "#customerId")
    public CustomerDTO getCustomerById(Long customerId) {
        logger.info("Fetching customer by ID: {}", customerId);
        Customer customer = customerRepository.findById(customerId).orElse(null);

        if (customer != null) {
            // Fetch family members separately to avoid recursive serialization
            List<Customer> familyMembers = customerRepository.findByParentId(customer.getId());
            customer.setFamilyMembers(familyMembers);

            logger.info("Fetched customer by ID successfully: {}", customer);
            return mappingUtils.convertToDTO(customer);
        } else {
            logger.warn("Customer not found by ID: {}", customerId);
            throw CustomerServiceException.builder()
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .developerMessage("Customer not found with ID: " + customerId)
                    .userMessage("Customer not found")
                    .build();
        }
    }

    @CacheEvict(value = "customers", key = "#customerId")
    @Transactional
    public CustomerDTO updateCustomer(Long customerId, CustomerDTO updatedCustomerDTO) {
        try {
            logger.info("Updating customer with ID: {}", customerId);
            Customer existingCustomer = customerRepository.findById(customerId).orElse(null);

            if (existingCustomer != null) {
                // Exclude the ID from mapping
                modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
                modelMapper.map(updatedCustomerDTO, existingCustomer);

                // Set the parent for each family member
                if (existingCustomer.getFamilyMembers() != null) {
                    existingCustomer.getFamilyMembers().forEach(familyMember -> {
                        familyMember.setParent(existingCustomer);
                    });

                    // Update the parent reference for all family members
                    customerRepository.saveAll(existingCustomer.getFamilyMembers());
                }

                // Save the updated customer (including family members with updated parent references)
                Customer updatedCustomer = customerRepository.save(existingCustomer);

                logger.info("Customer updated successfully: {}", updatedCustomer);
                return mappingUtils.convertToDTO(updatedCustomer);
            } else {
                throw CustomerServiceException.builder()
                        .httpStatus(HttpStatus.NOT_FOUND)
                        .developerMessage("Customer not found with ID: " + customerId)
                        .userMessage("Customer not found")
                        .build();
            }
        } catch (Exception e) {
            logger.error("Error updating customer", e);
            throw e;
        }
    }


    @Transactional
    public void deleteCustomer(Long customerId) {
        try {
            logger.info("Deleting customer with ID: {}", customerId);
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> CustomerServiceException.builder()
                            .httpStatus(HttpStatus.NOT_FOUND)
                            .developerMessage("Customer not found with ID: " + customerId)
                            .userMessage("Customer not found")
                            .build());

            logger.info("Deleting customer: {}", customer);

            // Delete the family members (cascading delete)
            if (customer.getFamilyMembers() != null) {
                customerRepository.deleteAll(customer.getFamilyMembers());
            }

            // Remove the parent reference from family members
            if (customer.getFamilyMembers() != null) {
                customer.getFamilyMembers().forEach(familyMember -> familyMember.setParent(null));
            }

            // Delete the main customer
            customerRepository.deleteById(customerId);

            logger.info("Customer and family members deleted successfully.");
        } catch (Exception e) {
            logger.error("Error deleting customer", e);
            throw e;
        }
    }

}

