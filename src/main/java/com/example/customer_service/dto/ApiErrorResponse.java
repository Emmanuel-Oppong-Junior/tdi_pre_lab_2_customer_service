package com.example.customer_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApiErrorResponse {
    private String message;
    private int status;
    private String timestamp;
    private Map<String, String> errors;
}
