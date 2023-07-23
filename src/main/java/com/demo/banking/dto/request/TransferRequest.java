package com.demo.banking.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TransferRequest {
    private Long fromAccountId;
    private Long toAccountId;
    private Double amount;
}
