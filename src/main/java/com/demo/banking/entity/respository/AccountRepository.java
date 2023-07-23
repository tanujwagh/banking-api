package com.demo.banking.entity.respository;

import com.demo.banking.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> getAllByCustomerIdEquals(Long id);
}
