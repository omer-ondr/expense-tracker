package com.expenseTracker.backend.dto;

import java.math.BigDecimal;

import com.expenseTracker.backend.entity.enums.AccountType;

import lombok.Data;

@Data
public class AccountDTO {
    private Long id;
    private String accountName;
    private AccountType accountType;
    private BigDecimal balance;
    private Long userId; 
}
