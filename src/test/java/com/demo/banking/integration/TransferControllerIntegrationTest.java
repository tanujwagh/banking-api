package com.demo.banking.integration;

import com.demo.banking.dto.response.TransferResponse;
import com.demo.banking.entity.Account;
import com.demo.banking.entity.Transfer;
import com.demo.banking.entity.respository.AccountRepository;
import com.demo.banking.entity.respository.TransferRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TransferControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllTransfers_success() throws Exception {
        mockMvc.perform(get("/transfers"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(20))
                .andExpect(jsonPath("$.totalItems").value(transferRepository.findAll().size()))
                .andExpect(jsonPath("$.totalPages").value(0));
    }

    @Test
    void testGetTransferById_success() throws Exception {
        String request = """
                    {
                        "fromAccountId": "%d",
                        "toAccountId": "%d",
                        "amount": %f
                    }
                """;

        Account fromAccount = accountRepository.findAll().get(0);
        Account toAccount = accountRepository.findAll().get(1);

        String result = mockMvc.perform(post("/transfers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request.formatted(fromAccount.getId(), toAccount.getId(), 500.0))
        ).andDo(print()).andReturn().getResponse().getContentAsString();

        TransferResponse transferResponse = objectMapper.readValue(result, TransferResponse.class);
        Transfer transfer = transferRepository.findAll().get(0);
        mockMvc.perform(get("/transfers/{id}", transfer.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(transfer.getId()))
                .andExpect(jsonPath("$.fromAccountId").value(transfer.getFromAccountId()))
                .andExpect(jsonPath("$.toAccountId").value(transfer.getToAccountId()))
                .andExpect(jsonPath("$.amount").value(transfer.getAmount()));
    }

    @Test
    void testTransfer_failure_insufficientBalance() throws Exception {
        String request = """
                    {
                        "fromAccountId": "%d",
                        "toAccountId": "%d",
                        "amount": %f
                    }
                """;

        Account fromAccount = accountRepository.findAll().get(0);
        Account toAccount = accountRepository.findAll().get(1);

        mockMvc.perform(post("/transfers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request.formatted(fromAccount.getId(), toAccount.getId(), 10000.0))
        )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("No sufficient balance to transfer"));
    }

    @Test
    void testGetTransferById_failure_transferNotFound() throws Exception {
        mockMvc.perform(get("/transfers/{id}", "100"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Transfer not found with id - 100"));
    }
}
