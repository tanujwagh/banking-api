package com.demo.banking.controller;

import com.demo.banking.dto.request.AccountRequest;
import com.demo.banking.dto.request.CustomerRequest;
import com.demo.banking.dto.response.AccountResponse;
import com.demo.banking.dto.response.CustomerResponse;
import com.demo.banking.dto.response.PageResponse;
import com.demo.banking.exception.NotFoundException;
import com.demo.banking.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }


    @GetMapping
    public PageResponse<CustomerResponse> getAllCustomers(@RequestParam(value = "page", defaultValue = "0") Integer page, @RequestParam(value = "size", defaultValue = "20") Integer size){
        PageRequest pageRequest =PageRequest.of(page, size);
        return customerService.getAllCustomers(pageRequest);
    }

    @GetMapping("/{id}")
    public CustomerResponse getCustomerById(@PathVariable("id") Long id) throws NotFoundException {
        return customerService.getCustomerById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerResponse createCustomer(@Valid @RequestBody CustomerRequest customerRequest){
        return customerService.createCustomer(customerRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteCustomer(@PathVariable("id") Long id) throws NotFoundException{
        customerService.deleteCustomer(id);
    }
}
