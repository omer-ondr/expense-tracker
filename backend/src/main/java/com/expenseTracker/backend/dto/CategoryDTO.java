package com.expenseTracker.backend.dto;

import lombok.Data;

@Data
public class CategoryDTO {
    private Long id;
    private String name;
    private String icon;
    private String color;
    // UserID'yi ekliyoruz ama tüm User objesini göndermiyoruz
    private Long userId;
}
