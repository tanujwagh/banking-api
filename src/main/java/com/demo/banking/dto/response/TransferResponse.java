package com.demo.banking.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class TransferResponse {
    private Long id;
    private Long fromAccountId;
    private Long toAccountId;
    private Double amount;
    private LocalDateTime timestamp;
}
