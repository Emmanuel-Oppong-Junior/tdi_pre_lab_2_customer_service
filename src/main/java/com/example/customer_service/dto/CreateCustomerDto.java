package com.example.customer_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class CreateCustomerDto {
    @NotBlank
    String name;

    @NotBlank
    @Email
    String email;

    String phone;
}
