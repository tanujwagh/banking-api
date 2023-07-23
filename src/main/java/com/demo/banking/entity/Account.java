package com.demo.banking.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Account implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private Long customerId;

    @Column(nullable = false)
    private Double balance;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Builder
    public Account(Long customerId, AccountType accountType, Double balance) {
        this.customerId = customerId;
        this.accountType = accountType;
        this.balance = balance;
    }
}
