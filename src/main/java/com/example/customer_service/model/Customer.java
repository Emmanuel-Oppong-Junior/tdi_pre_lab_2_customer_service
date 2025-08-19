package com.example.customer_service.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "customers")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String phone;
}
