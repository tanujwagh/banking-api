package com.demo.banking.service;

import com.demo.banking.dto.request.TransferRequest;
import com.demo.banking.dto.response.AccountTransferResponse;
import com.demo.banking.dto.response.PageResponse;
import com.demo.banking.dto.response.TransferResponse;
import com.demo.banking.dto.response.TransferType;
import com.demo.banking.entity.Account;
import com.demo.banking.entity.Transfer;
import com.demo.banking.entity.respository.AccountRepository;
import com.demo.banking.entity.respository.TransferRepository;
import com.demo.banking.exception.NotFoundException;
import com.demo.banking.exception.TransferException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransferService {

    private final TransferRepository transferRepository;
    private final AccountRepository accountRepository;

    public TransferService(TransferRepository transferRepository, AccountRepository accountRepository) {
        this.transferRepository = transferRepository;
        this.accountRepository = accountRepository;
    }

    public PageResponse<TransferResponse> getAllTransfers(PageRequest pageRequest) {
        Page<TransferResponse> page = transferRepository.findAll(pageRequest)
                .map(transfer -> new TransferResponse(
                                transfer.getId(),
                                transfer.getFromAccountId(),
                                transfer.getToAccountId(),
                                transfer.getAmount(),
                                transfer.getTimestamp()
                        )
                );
        return PageResponse.<TransferResponse>builder()
                .items(page.getContent())
                .size(page.getSize())
                .page(page.getNumber())
                .totalItems(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    public TransferResponse getTransferById(Long id) throws NotFoundException {
        return transferRepository.findById(id)
                .map(transfer -> new TransferResponse(
                                transfer.getId(),
                                transfer.getFromAccountId(),
                                transfer.getToAccountId(),
                                transfer.getAmount(),
                                transfer.getTimestamp()
                        )
                ).orElseThrow(() -> new NotFoundException("Transfer", id));
    }

    public List<AccountTransferResponse> getAllTransferByAccountId(Long id) throws NotFoundException {
        Long accountId = accountRepository.findById(id).map(Account::getId).orElseThrow(() -> new NotFoundException("Account", id));
        return transferRepository.findAllByFromAccountIdEqualsOrToAccountIdEquals(id, id).stream()
                .map(transfer -> new AccountTransferResponse(
                                transfer.getId(),
                                transfer.getFromAccountId(),
                                transfer.getToAccountId(),
                                transfer.getAmount(),
                                transfer.getTimestamp(),
                                transfer.getFromAccountId().equals(id) ? TransferType.DEBIT : TransferType.CREDIT
                        )
                ).collect(Collectors.toList());
    }

    @Transactional
    public TransferResponse executeTransfer(TransferRequest transferRequest) throws NotFoundException, TransferException {
        Account fromAccount = accountRepository.findById(transferRequest.getFromAccountId()).orElseThrow(() -> new NotFoundException("Account", transferRequest.getFromAccountId()));

        Account toAccount = accountRepository.findById(transferRequest.getToAccountId()).orElseThrow(() -> new NotFoundException("Account", transferRequest.getToAccountId()));

        if (fromAccount.getBalance().compareTo(transferRequest.getAmount()) <= 0) {
            throw new TransferException("No sufficient balance to transfer");
        }

        Transfer transfer = Transfer.builder()
                .fromAccountId(fromAccount.getId())
                .toAccountId(toAccount.getId())
                .amount(transferRequest.getAmount())

                .build();

        fromAccount.setBalance(fromAccount.getBalance() - transfer.getAmount());
        toAccount.setBalance(toAccount.getBalance() + transfer.getAmount());

        accountRepository.save(toAccount);
        accountRepository.save(fromAccount);

        transfer = transferRepository.saveAndFlush(transfer);
        return new TransferResponse(
                transfer.getId(),
                transfer.getFromAccountId(),
                transfer.getToAccountId(),
                transfer.getAmount(),
                transfer.getTimestamp()
        );
    }
}
