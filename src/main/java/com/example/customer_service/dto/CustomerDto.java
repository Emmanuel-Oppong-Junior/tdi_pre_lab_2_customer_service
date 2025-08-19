package com.example.customer_service.dto;

public record CustomerDto(
        Long id,
        String name,
        String email,
        String phone
) {
}
