package com.demo.banking.controller;

import com.demo.banking.dto.request.AccountRequest;
import com.demo.banking.dto.response.AccountResponse;
import com.demo.banking.dto.response.PageResponse;
import com.demo.banking.exception.NotFoundException;
import com.demo.banking.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }


    @GetMapping
    public PageResponse<AccountResponse> getAllAccounts(@RequestParam(value = "page", defaultValue = "0") Integer page, @RequestParam(value = "size", defaultValue = "20") Integer size){
        PageRequest pageRequest =PageRequest.of(page, size);
        return accountService.getAllAccounts(pageRequest);
    }

    @GetMapping("/{id}")
    public AccountResponse getAccountById(@PathVariable("id") Long id) throws NotFoundException {
        return accountService.getAccountById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountResponse createAccount(@Valid @RequestBody AccountRequest accountRequest){
        return accountService.createAccount(accountRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void createAccount(@PathVariable("id") Long id) throws NotFoundException{
        accountService.deleteAccount(id);
    }
}
