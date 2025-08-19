package com.example.customer_service;

import com.example.customer_service.dto.CreateCustomerDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerServiceApplicationTests {
    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    void contextLoads() {
    }

    @Test
    void testCreateNewCustomer() {
        String url = "/customers";
        CreateCustomerDto createCustomerDto = new CreateCustomerDto();
        createCustomerDto.setName("John Doe");
        createCustomerDto.setEmail("john.doe@gmail.com");
        createCustomerDto.setPhone("1234567890");
        var result = restTemplate.postForEntity(url, createCustomerDto, String.class);
        assert result.getStatusCode().is2xxSuccessful();
    }

    @Test
    void testGetAllCustomers() {
        String url = "/customers";
        CreateCustomerDto createCustomerDto = new CreateCustomerDto();
        createCustomerDto.setName("John Doe");
        createCustomerDto.setEmail("john.doe@gmail.com");
        createCustomerDto.setPhone("1234567890");
        //create customer
        restTemplate.postForEntity(url, createCustomerDto, String.class);


        var result = restTemplate.getForEntity(url, String.class);
        assert result.getStatusCode().is2xxSuccessful();
        System.out.println(result.getBody());
        assert result.getBody() != null && !result.getBody().isEmpty();
    }
}
