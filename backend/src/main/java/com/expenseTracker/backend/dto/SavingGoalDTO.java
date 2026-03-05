package com.expenseTracker.backend.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class SavingGoalDTO {
    private Long id;
    private String goalName;         
    private BigDecimal targetAmount; 
    private BigDecimal currentAmount;
    private LocalDate deadline;      
    private Long userId;
}
