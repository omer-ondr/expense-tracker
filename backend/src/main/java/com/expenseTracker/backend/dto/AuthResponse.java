package com.expenseTracker.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;      
    private Long userId;       
    private String firstName;
    private String lastName;
}