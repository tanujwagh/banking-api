package com.demo.banking.dto.response;

import com.demo.banking.entity.AccountType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AccountResponse {
    private Long id;
    private Long customerId;
    private Double balance;
    private AccountType accountType;
}
