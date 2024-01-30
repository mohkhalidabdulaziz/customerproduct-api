package com.customerproduct.customerproductapi.utils;

import com.customerproduct.customerproductapi.dto.CustomerDTO;
import com.customerproduct.customerproductapi.entity.Customer;
import com.customerproduct.customerproductapi.exception.CustomerServiceException;
import com.customerproduct.customerproductapi.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MappingUtils {

    private final ModelMapper modelMapper;
    private final CustomerRepository customerRepository;

    @PostConstruct
    public void configure() {
        TypeMap<Customer, CustomerDTO> typeMap = modelMapper.createTypeMap(Customer.class, CustomerDTO.class);
        typeMap.addMappings(mapper -> mapper.using(familyMembersConverter()).map(Customer::getFamilyMembers, CustomerDTO::setFamilyMembers));
    }

    public CustomerDTO convertToDTO(Customer customer) {
        return modelMapper.map(customer, CustomerDTO.class);
    }

    public Customer convertToEntity(CustomerDTO customerDTO) {
        return modelMapper.map(customerDTO, Customer.class);
    }

    private Converter<List<Customer>, List<CustomerDTO>> familyMembersConverter() {
        return ctx -> ctx.getSource() == null ? null : ctx.getSource().stream()
                .map(customer -> convertToDTO(customer))
                .collect(Collectors.toList());
    }

    public boolean validateEmailNotExists(String email) {
        return !customerRepository.findByEmail(email).isPresent();
    }
}
