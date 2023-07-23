package com.demo.banking.service;

import com.demo.banking.dto.request.CustomerRequest;
import com.demo.banking.dto.response.CustomerDetailsResponse;
import com.demo.banking.dto.response.CustomerResponse;
import com.demo.banking.dto.response.PageResponse;
import com.demo.banking.entity.Account;
import com.demo.banking.entity.Customer;
import com.demo.banking.entity.respository.AccountRepository;
import com.demo.banking.entity.respository.CustomerRepository;
import com.demo.banking.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;

    public CustomerService(CustomerRepository customerRepository, AccountRepository accountRepository) {
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
    }

    public PageResponse<CustomerResponse> getAllCustomers(PageRequest pageRequest) {
        Page<CustomerResponse> page = customerRepository.findAll(pageRequest)
                .map(customer -> new CustomerResponse(
                                customer.getId(),
                                customer.getName()
                        )
                );
        return PageResponse.<CustomerResponse>builder()
                .items(page.getContent())
                .size(page.getSize())
                .page(page.getNumber())
                .totalItems(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    public CustomerDetailsResponse getCustomerById(Long id) throws NotFoundException {
        return customerRepository.findById(id)
                .map(customer -> new CustomerDetailsResponse(
                                customer.getId(),
                                customer.getName(),
                                accountRepository.getAllByCustomerIdEquals(id).stream().map(Account::getId).toList()
                        )
                )
                .orElseThrow(() -> new NotFoundException("Customer", id));
    }

    public CustomerDetailsResponse createCustomer(CustomerRequest accountRequest) {
        Customer customer = Customer.builder()
                .name(accountRequest.getName())
                .build();

        customer = customerRepository.save(customer);
        return new CustomerDetailsResponse(
                customer.getId(),
                customer.getName(),
                accountRepository.getAllByCustomerIdEquals(customer.getId()).stream().map(Account::getId).toList()
        );
    }

    public void deleteCustomer(Long id) throws NotFoundException {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new NotFoundException("Customer", id));
        customerRepository.delete(customer);
    }
}
