package com.example.customer_service.service;

import com.example.customer_service.dto.CreateCustomerDto;
import com.example.customer_service.dto.CustomerDto;
import com.example.customer_service.dto.UpdateCustomerDto;
import com.example.customer_service.exception.NotFoundException;
import com.example.customer_service.model.Customer;
import com.example.customer_service.respository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerDto saveCustomer(CreateCustomerDto dto) {
        Customer customer = Customer.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .build();
        Customer savedCustomer = this.customerRepository.save(customer);
        return toDto(savedCustomer);
    }


    public CustomerDto updateCustomer(UpdateCustomerDto dto) {
        Customer existingCustomer = this.customerRepository.findById(dto.getId())
                .orElseThrow(() -> new NotFoundException("Customer not found with id: " + dto.getId()));
        if (dto.getName() != null && !dto.getName().isBlank()) {
            existingCustomer.setName(dto.getName());
        }
        if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
            existingCustomer.setEmail(dto.getEmail());
        }
        if (dto.getPhone() != null && !dto.getPhone().isBlank()) {
            existingCustomer.setPhone(dto.getPhone());
        }
        return toDto(this.customerRepository.save(existingCustomer));
    }

    public void deleteCustomer(Long id) {
        this.customerRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Customer not found with id: " + id));
        this.customerRepository.deleteById(id);
    }

    public CustomerDto getCustomerById(Long id) {
        Customer customer = this.customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer not found with id: " + id));
        return toDto(customer);
    }

    public List<CustomerDto> getAllCustomers() {
        return toDto(this.customerRepository.findAll());
    }

    private CustomerDto toDto(Customer customer) {
        return new CustomerDto(customer.getId(), customer.getName(), customer.getEmail(), customer.getPhone());
    }

    private List<CustomerDto> toDto(List<Customer> customers) {
        return customers.stream()
                .map(customer -> new CustomerDto(customer.getId(), customer.getName(), customer.getEmail(), customer.getPhone()))
                .toList();
    }
}
