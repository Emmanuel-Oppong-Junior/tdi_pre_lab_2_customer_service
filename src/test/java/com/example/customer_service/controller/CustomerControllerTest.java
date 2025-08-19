package com.example.customer_service.controller;

import com.example.customer_service.dto.CreateCustomerDto;
import com.example.customer_service.dto.CustomerDto;
import com.example.customer_service.dto.UpdateCustomerDto;
import com.example.customer_service.exception.GlobalException;
import com.example.customer_service.exception.NotFoundException;
import com.example.customer_service.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private CustomerDto customerDto;
    private CreateCustomerDto createCustomerDto;
    private UpdateCustomerDto updateCustomerDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(customerController)
                .setControllerAdvice(new GlobalException()).build();
        objectMapper = new ObjectMapper();

        customerDto = new CustomerDto(1L, "John Doe", "john.doe@example.com", "1234567890");
        createCustomerDto = new CreateCustomerDto();
        createCustomerDto.setName("John Doe");
        createCustomerDto.setEmail("john.doe@example.com");
        createCustomerDto.setPhone("1234567890");

        updateCustomerDto = new UpdateCustomerDto();
        updateCustomerDto.setId(1L);
        updateCustomerDto.setName("Jane Doe");
        updateCustomerDto.setEmail("jane.doe@example.com");
        updateCustomerDto.setPhone("0987654321");
    }

    @Test
    void saveCustomer_ShouldReturnCreatedStatusAndLocation_WhenValidDtoProvided() throws Exception {
        when(customerService.saveCustomer(any(CreateCustomerDto.class))).thenReturn(customerDto);

        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCustomerDto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/customers/1"));

        verify(customerService).saveCustomer(any(CreateCustomerDto.class));
    }

    @Test
    void updateCustomer_ShouldReturnOkStatusAndUpdatedCustomer_WhenValidDtoProvided() throws Exception {
        when(customerService.updateCustomer(any(UpdateCustomerDto.class))).thenReturn(customerDto);

        mockMvc.perform(put("/customers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateCustomerDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(customerDto.id()))
                .andExpect(jsonPath("$.name").value(customerDto.name()))
                .andExpect(jsonPath("$.email").value(customerDto.email()))
                .andExpect(jsonPath("$.phone").value(customerDto.phone()));

        verify(customerService).updateCustomer(any(UpdateCustomerDto.class));
    }

    @Test
    void updateCustomer_ShouldReturnNotFound_WhenCustomerNotFound() throws Exception {
        when(customerService.updateCustomer(any(UpdateCustomerDto.class)))
                .thenThrow(new NotFoundException("Customer not found with id: 1"));

        mockMvc.perform(put("/customers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateCustomerDto)))
                .andExpect(status().isNotFound());

        verify(customerService).updateCustomer(any(UpdateCustomerDto.class));
    }

    @Test
    void deleteCustomer_ShouldReturnNoContent_WhenCustomerExists() throws Exception {
        doNothing().when(customerService).deleteCustomer(1L);

        mockMvc.perform(delete("/customers/1"))
                .andExpect(status().isNoContent());

        verify(customerService).deleteCustomer(1L);
    }

    @Test
    void deleteCustomer_ShouldReturnNotFound_WhenCustomerNotFound() throws Exception {
        doThrow(new NotFoundException("Customer not found with id: 1")).when(customerService).deleteCustomer(1L);

        mockMvc.perform(delete("/customers/1"))
                .andExpect(status().isNotFound());

        verify(customerService).deleteCustomer(1L);
    }

    @Test
    void getCustomerById_ShouldReturnOkStatusAndCustomer_WhenCustomerExists() throws Exception {
        when(customerService.getCustomerById(1L)).thenReturn(customerDto);

        mockMvc.perform(get("/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(customerDto.id()))
                .andExpect(jsonPath("$.name").value(customerDto.name()))
                .andExpect(jsonPath("$.email").value(customerDto.email()))
                .andExpect(jsonPath("$.phone").value(customerDto.phone()));

        verify(customerService).getCustomerById(1L);
    }

    @Test
    void getCustomerById_ShouldReturnNotFound_WhenCustomerNotFound() throws Exception {
        when(customerService.getCustomerById(1L))
                .thenThrow(new NotFoundException("Customer not found with id: 1"));

        mockMvc.perform(get("/customers/1"))
                .andExpect(status().isNotFound());

        verify(customerService).getCustomerById(1L);
    }

    @Test
    void getAllCustomers_ShouldReturnOkStatusAndCustomerList_WhenCustomersExist() throws Exception {
        List<CustomerDto> customers = List.of(customerDto);
        when(customerService.getAllCustomers()).thenReturn(customers);

        mockMvc.perform(get("/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(customerDto.id()))
                .andExpect(jsonPath("$[0].name").value(customerDto.name()))
                .andExpect(jsonPath("$[0].email").value(customerDto.email()))
                .andExpect(jsonPath("$[0].phone").value(customerDto.phone()));

        verify(customerService).getAllCustomers();
    }

    @Test
    void getAllCustomers_ShouldReturnOkStatusAndEmptyList_WhenNoCustomersExist() throws Exception {
        when(customerService.getAllCustomers()).thenReturn(List.of());

        mockMvc.perform(get("/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(customerService).getAllCustomers();
    }
}