package com.expenseTracker.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class BudgetDTO {
    private Long id;
    private BigDecimal amountLimit; 
    private BigDecimal spentAmount; 
    private LocalDate startDate;    
    private LocalDate endDate;      
    
    private Long categoryId;
    private String categoryName;    
    private Long userId;
}
