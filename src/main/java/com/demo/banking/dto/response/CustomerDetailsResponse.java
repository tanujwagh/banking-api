package com.demo.banking.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CustomerDetailsResponse extends CustomerResponse{
    private List<Long> accounts;

    public CustomerDetailsResponse(Long id, String name, List<Long> accounts) {
        super(id, name);
        this.accounts = accounts;
    }
}
