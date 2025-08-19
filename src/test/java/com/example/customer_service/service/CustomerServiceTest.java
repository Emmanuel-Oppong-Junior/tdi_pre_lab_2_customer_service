package com.example.customer_service.service;

import com.example.customer_service.dto.CreateCustomerDto;
import com.example.customer_service.dto.CustomerDto;
import com.example.customer_service.dto.UpdateCustomerDto;
import com.example.customer_service.exception.NotFoundException;
import com.example.customer_service.model.Customer;
import com.example.customer_service.respository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private Customer customer;
    private CreateCustomerDto createCustomerDto;
    private UpdateCustomerDto updateCustomerDto;

    @BeforeEach
    void setUp() {
        customer = Customer.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .phone("1234567890")
                .build();

        createCustomerDto = new CreateCustomerDto();
        createCustomerDto.setName("John Doe");
        createCustomerDto.setEmail("john@example.com");
        createCustomerDto.setPhone("1234567890");

        updateCustomerDto = new UpdateCustomerDto();
        updateCustomerDto.setId(1L);
        updateCustomerDto.setName("Jane Doe");
        updateCustomerDto.setEmail("jane@example.com");
        updateCustomerDto.setPhone("0987654321");
    }

    @Test
    void saveCustomer_ShouldReturnCustomerDto() {
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerDto result = customerService.saveCustomer(createCustomerDto);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("John Doe", result.name());
        assertEquals("john@example.com", result.email());
        assertEquals("1234567890", result.phone());
        verify(customerRepository).save(any(Customer.class));
    }


    @Test
    void

    updateCustomer_ShouldUpdateAndReturnCustomerDto() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerDto result = customerService.updateCustomer(updateCustomerDto);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Jane Doe", result.name());
        assertEquals("jane@example.com", result.email());
        assertEquals("0987654321", result.phone());
        verify(customerRepository).findById(1L);
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void updateCustomer_ShouldThrowNotFoundException_WhenCustomerNotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            customerService.updateCustomer(updateCustomerDto);
        });

        assertEquals("Customer not found with id: 1", exception.getMessage());
        verify(customerRepository).findById(1L);
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void updateCustomer_ShouldOnlyUpdateProvidedFields() {
        // Arrange
        Customer existingCustomer = Customer.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .phone("1234567890")
                .build();

        UpdateCustomerDto partialUpdateDto = new UpdateCustomerDto();
        partialUpdateDto.setId(1L);
        partialUpdateDto.setName("Jane Doe");
        // Email and phone not provided, should remain unchanged

        Customer expectedSavedCustomer = Customer.builder()
                .id(1L)
                .name("Jane Doe")
                .email("john@example.com") // unchanged
                .phone("1234567890") // unchanged
                .build();

        when(customerRepository.findById(1L)).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.save(any(Customer.class))).thenReturn(expectedSavedCustomer);

        // Act
        CustomerDto result = customerService.updateCustomer(partialUpdateDto);

        // Assert
        assertNotNull(result);
        assertEquals("Jane Doe", result.name());
        assertEquals("john@example.com", result.email());
        assertEquals("1234567890", result.phone());
        verify(customerRepository).findById(1L);
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void deleteCustomer_ShouldDeleteSuccessfully() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        doNothing().when(customerRepository).deleteById(1L);

        customerService.deleteCustomer(1L);

        verify(customerRepository).findById(1L);
        verify(customerRepository).deleteById(1L);
    }

    @Test
    void deleteCustomer_ShouldThrowNotFoundException_WhenCustomerNotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            customerService.deleteCustomer(1L);
        });

        assertEquals("Customer not found with id: 1", exception.getMessage());
        verify(customerRepository).findById(1L);
        verify(customerRepository, never()).deleteById(1L);
    }

    @Test
    void getCustomerById_ShouldReturnCustomerDto() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        CustomerDto result = customerService.getCustomerById(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("John Doe", result.name());
        assertEquals("john@example.com", result.email());
        assertEquals("1234567890", result.phone());
        verify(customerRepository).findById(1L);
    }

    @Test
    void getCustomerById_ShouldThrowNotFoundException_WhenCustomerNotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            customerService.getCustomerById(1L);
        });

        assertEquals("Customer not found with id: 1", exception.getMessage());
        verify(customerRepository).findById(1L);
    }

    @Test
    void getAllCustomers_ShouldReturnCustomerDtoList() {
        List<Customer> customers = Arrays.asList(
                customer,
                Customer.builder().id(2L).name("Jane Doe").email("jane@example.com").phone("0987654321").build()
        );
        when(customerRepository.findAll()).thenReturn(customers);

        List<CustomerDto> result = customerService.getAllCustomers();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).id());
        assertEquals("John Doe", result.get(0).name());
        assertEquals(2L, result.get(1).id());
        assertEquals("Jane Doe", result.get(1).name());
        verify(customerRepository).findAll();
    }
}