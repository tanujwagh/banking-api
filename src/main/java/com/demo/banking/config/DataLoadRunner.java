package com.demo.banking.config;

import com.demo.banking.entity.Account;
import com.demo.banking.entity.AccountType;
import com.demo.banking.entity.Customer;
import com.demo.banking.entity.respository.AccountRepository;
import com.demo.banking.entity.respository.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Stream;

@Component
public class DataLoadRunner implements CommandLineRunner {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;

    public DataLoadRunner(AccountRepository accountRepository, CustomerRepository customerRepository) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        List<Customer> customers = Stream.of(
                Pair.of(1L, "Arisha Barron"),
                Pair.of(2L, "Branden Gibson"),
                Pair.of(3L, "Rhonda Church"),
                Pair.of(4L, "Georgina Hazel")
        ).map(customer -> Customer.builder()
                .id(customer.getFirst())
                .name(customer.getSecond())
                .build()
        ).toList();

        customerRepository.saveAll(customers);

        List<Account> accounts = customers.stream().map(customer -> Account.builder()
                .customerId(customer.getId())
                .accountType(AccountType.USD)
                .balance(1000.0)
                .build()
        ).toList();

        accountRepository.saveAll(accounts);
    }
}
