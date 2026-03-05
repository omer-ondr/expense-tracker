package com.expenseTracker.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;      // Ürettiğimiz o meşhur JWT
    private Long userId;       // Frontend (Next.js) bu ID ile harcamaları çekecek
    private String firstName;
    private String lastName;
}