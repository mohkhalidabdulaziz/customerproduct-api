package com.customerproduct.customerproductapi;

import com.customerproduct.customerproductapi.controller.CustomerController;
import com.customerproduct.customerproductapi.dto.CustomerDTO;
import com.customerproduct.customerproductapi.service.CustomerService;
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

class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testCreateCustomer() throws Exception {
        CustomerDTO requestDTO = createTestCustomerDTO();
        when(customerService.createCustomer(any())).thenReturn(createTestCustomerDTO());

        mockMvc.perform(MockMvcRequestBuilders.post("/customerproduct-api/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(16));

        verify(customerService, times(1)).createCustomer(any());
    }

    @Test
    void testGetAllCustomers() throws Exception {
        when(customerService.getAllCustomers()).thenReturn(Collections.singletonList(createTestCustomerDTO()));

        mockMvc.perform(MockMvcRequestBuilders.get("/customerproduct-api/api/customers"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").value(16));

        verify(customerService, times(1)).getAllCustomers();
    }

  @Test
  void testGetCustomerById() throws Exception {
      Long customerId = 16L;

      // Mock the service response
      CustomerDTO mockResponse = createTestCustomerDTO();
      when(customerService.getCustomerById(customerId)).thenReturn(mockResponse);

      mockMvc.perform(MockMvcRequestBuilders.get("/customerproduct-api/api/customers/{customerId}", customerId))
              .andExpect(MockMvcResultMatchers.status().isOk())
              .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
              .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(16))
              .andExpect(MockMvcResultMatchers.jsonPath("$.data.firstName").value("Test"))
              .andExpect(MockMvcResultMatchers.jsonPath("$.data.lastName").value("Creeate User Test "))
              .andExpect(MockMvcResultMatchers.jsonPath("$.data.email").value("test-create@example.com"))
              .andExpect(MockMvcResultMatchers.jsonPath("$.data.phoneNumber").value("+1234567890"))
              .andExpect(MockMvcResultMatchers.jsonPath("$.data.familyMembers[0].id").value(17))
              .andExpect(MockMvcResultMatchers.jsonPath("$.data.familyMembers[0].firstName").value("Test child"))
              .andExpect(MockMvcResultMatchers.jsonPath("$.data.familyMembers[0].lastName").value("Member"))
              .andExpect(MockMvcResultMatchers.jsonPath("$.data.familyMembers[0].email").value("test-child@example.com"))
              .andExpect(MockMvcResultMatchers.jsonPath("$.data.familyMembers[0].phoneNumber").value("+1234567891"));

      // Verify that the service method was called
      verify(customerService, times(1)).getCustomerById(customerId);
  }

    @Test
    void testUpdateCustomer() throws Exception {
        Long customerId = 16L;
        CustomerDTO updatedDTO = createTestCustomerDTO();
        when(customerService.updateCustomer(eq(customerId), any())).thenReturn(createTestCustomerDTO());

        mockMvc.perform(MockMvcRequestBuilders.put("/customerproduct-api/api/customers/{customerId}", customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(16));

        verify(customerService, times(1)).updateCustomer(eq(customerId), any());
    }

    @Test
    void testDeleteCustomer() throws Exception {
        Long customerId = 16L;
        mockMvc.perform(MockMvcRequestBuilders.delete("/customerproduct-api/api/customers/{customerId}", customerId))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(customerService, times(1)).deleteCustomer(customerId);
    }


    private CustomerDTO createTestCustomerDTO() {
        return CustomerDTO.builder()
                .id(16L)
                .firstName("Test")
                .lastName("Creeate User Test ")
                .email("test-create@example.com")
                .phoneNumber("+1234567890")
                .familyMembers(Collections.singletonList(
                        CustomerDTO.builder()
                                .id(17L)
                                .firstName("Test child")
                                .lastName("Member")
                                .email("test-child@example.com")
                                .phoneNumber("+1234567891")
                                .familyMembers(Collections.emptyList())
                                .build()
                ))
                .build();
    }
}
