package com.demo.banking.integration;

import com.demo.banking.entity.Customer;
import com.demo.banking.entity.respository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomerControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void testGetAllCustomer_success() throws Exception {
        mockMvc.perform(get("/customers"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(20))
                .andExpect(jsonPath("$.totalItems").value(customerRepository.findAll().size()))
                .andExpect(jsonPath("$.totalPages").value(1));
    }

    @Test
    void testGetCustomerById_success() throws Exception {
        Customer customer = customerRepository.findAll().get(0);
        mockMvc.perform(get("/customers/{id}", customer.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(customer.getId()))
                .andExpect(jsonPath("$.name").value(customer.getName()));
    }

    @Test
    void testGetCustomerById_failure_customerNotFound() throws Exception {
        mockMvc.perform(get("/customers/{id}", "100"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Customer not found with id - 100"));
    }
}
