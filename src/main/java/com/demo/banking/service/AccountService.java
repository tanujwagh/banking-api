package com.demo.banking.service;

import com.demo.banking.dto.request.AccountRequest;
import com.demo.banking.dto.response.AccountResponse;
import com.demo.banking.dto.response.PageResponse;
import com.demo.banking.entity.Account;
import com.demo.banking.entity.AccountType;
import com.demo.banking.entity.respository.AccountRepository;
import com.demo.banking.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public PageResponse<AccountResponse> getAllAccounts(PageRequest pageRequest) {
        Page<AccountResponse> page = accountRepository.findAll(pageRequest)
                .map(account -> AccountResponse.builder()
                        .id(account.getId())
                        .customerId(account.getCustomerId())
                        .accountType(account.getAccountType())
                        .balance(account.getBalance())
                        .build()
                );
        return PageResponse.<AccountResponse>builder()
                .items(page.getContent())
                .size(page.getSize())
                .page(page.getNumber())
                .totalItems(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    public AccountResponse getAccountById(Long id) throws NotFoundException {
        return accountRepository.findById(id).map(account -> AccountResponse.builder()
                .id(account.getId())
                .customerId(account.getCustomerId())
                .accountType(account.getAccountType())
                .balance(account.getBalance())
                .build()
        ).orElseThrow(() -> new NotFoundException("Account", id));
    }

    public AccountResponse createAccount(AccountRequest accountRequest) {
        Account account = Account.builder()
                .balance(accountRequest.getBalance())
                .customerId(accountRequest.getCustomerId())
                .accountType(AccountType.USD)
                .build();
        account = accountRepository.save(account);
        return AccountResponse.builder()
                .id(account.getId())
                .customerId(account.getCustomerId())
                .balance(account.getBalance())
                .accountType(account.getAccountType())
                .build();
    }

    public void deleteAccount(Long id) throws NotFoundException {
        Account account = accountRepository.findById(id).orElseThrow(() -> new NotFoundException("Account", id));
        accountRepository.delete(account);
    }
}
