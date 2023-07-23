package com.demo.banking.entity.respository;

import com.demo.banking.entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.stream.Stream;

public interface TransferRepository extends JpaRepository<Transfer, Long> {
    List<Transfer> findAllByFromAccountIdEqualsOrToAccountIdEquals(Long fromAccountId, Long toAccountId);
}
