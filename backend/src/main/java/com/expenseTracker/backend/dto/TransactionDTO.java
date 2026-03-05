package com.expenseTracker.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.expenseTracker.backend.entity.enums.TransactionType;

import lombok.Data;

@Data
public class TransactionDTO {
    private Long id;
    private BigDecimal amount;
    private String description;
    private LocalDateTime transactionDate;
    private TransactionType transactionType;
    
    
    private Long accountId;
    private String accountName;
    private Long categoryId;
    private String categoryName;
    private Long userId;
}
