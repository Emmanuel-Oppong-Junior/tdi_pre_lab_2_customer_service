package com.example.customer_service.controller;

import com.example.customer_service.dto.CreateCustomerDto;
import com.example.customer_service.dto.CustomerDto;
import com.example.customer_service.dto.UpdateCustomerDto;
import com.example.customer_service.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customers")
public class CustomerController {
    private final Logger logger = LoggerFactory.getLogger(CustomerController.class);
    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<Void> saveCustomer(@Valid @RequestBody CreateCustomerDto createCustomerDto) {
        CustomerDto createdCustomer = customerService.saveCustomer(createCustomerDto);
        URI location = URI.create("/customers/" + createdCustomer.id());
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDto> updateCustomer(@Valid @RequestBody UpdateCustomerDto updateCustomerDto) {
        CustomerDto updatedCustomer = customerService.updateCustomer(updateCustomerDto);
        return ResponseEntity.ok(updatedCustomer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable Long id) {
        CustomerDto customer = customerService.getCustomerById(id);
        return ResponseEntity.ok(customer);
    }

    @GetMapping
    public ResponseEntity<List<CustomerDto>> getAllCustomers(@RequestHeader(value = "X-Username", required = false) String username) {
        logger.info("username {}", username);
        List<CustomerDto> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }
}
