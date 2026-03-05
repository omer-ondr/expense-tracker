package com.expenseTracker.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.expenseTracker.backend.dto.CategoryDTO;
import com.expenseTracker.backend.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "http://localhost:3000") // Next.js bağlantısı için şart
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // Yeni Kategori Ekle
    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO categoryDTO) {
        return new ResponseEntity<>(categoryService.createCategory(categoryDTO), HttpStatus.CREATED);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CategoryDTO>> getCategoriesByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(categoryService.getCategoriesByUserId(userId));
    }
}