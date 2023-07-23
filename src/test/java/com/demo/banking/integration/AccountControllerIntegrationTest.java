package com.demo.banking.integration;

import com.demo.banking.entity.Account;
import com.demo.banking.entity.respository.AccountRepository;
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
public class AccountControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void testGetAllAccounts_success() throws Exception {
        mockMvc.perform(get("/accounts"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(20))
                .andExpect(jsonPath("$.totalItems").value(accountRepository.findAll().size()))
                .andExpect(jsonPath("$.totalPages").value(1));
    }

    @Test
    void testGetAccountById_success() throws Exception {
        Account account = accountRepository.findAll().get(0);
        mockMvc.perform(get("/accounts/{id}", account.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(account.getId()))
                .andExpect(jsonPath("$.customerId").value(account.getCustomerId()))
                .andExpect(jsonPath("$.accountType").value("USD"));
    }

    @Test
    void testGetAccountById_failure_AccountNotFound() throws Exception {
        mockMvc.perform(get("/accounts/{id}", "100"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Account not found with id - 100"));
    }
}
