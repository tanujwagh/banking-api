package com.demo.banking.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
public class AccountTransferResponse extends TransferResponse{

    private TransferType transferType;

    public AccountTransferResponse(Long id, Long fromAccountId, Long toAccountId, Double amount, LocalDateTime timestamp, TransferType transferType) {
        super(id, fromAccountId, toAccountId, amount, timestamp);
        this.transferType = transferType;
    }
}
