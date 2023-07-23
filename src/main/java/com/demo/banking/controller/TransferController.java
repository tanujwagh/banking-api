package com.demo.banking.controller;

import com.demo.banking.dto.request.TransferRequest;
import com.demo.banking.dto.response.AccountTransferResponse;
import com.demo.banking.dto.response.PageResponse;
import com.demo.banking.dto.response.TransferResponse;
import com.demo.banking.exception.NotFoundException;
import com.demo.banking.exception.TransferException;
import com.demo.banking.service.TransferService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transfers")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @GetMapping
    public PageResponse<TransferResponse> getAllTransfers(@RequestParam(value = "page", defaultValue = "0") Integer page, @RequestParam(value = "size", defaultValue = "20") Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return transferService.getAllTransfers(pageRequest);
    }

    @GetMapping("/{id}")
    public TransferResponse getTransferById(@PathVariable("id") Long id) throws NotFoundException {
        return transferService.getTransferById(id);
    }

    @GetMapping("/account/{id}")
    public List<AccountTransferResponse> getAllTransfersByAccountId(@PathVariable("id") Long id) throws NotFoundException {
        return transferService.getAllTransferByAccountId(id);
    }

    @PostMapping
    public TransferResponse executeTransfer(@Valid @RequestBody TransferRequest transferRequest) throws TransferException, NotFoundException {
        return transferService.executeTransfer(transferRequest);
    }

}