package com.expenseTracker.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.expenseTracker.backend.dto.BudgetDTO;
import com.expenseTracker.backend.service.BudgetService;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
@CrossOrigin(origins = "http://localhost:3000")
public class BudgetController {

    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @PostMapping
    public ResponseEntity<BudgetDTO> createBudget(@RequestBody BudgetDTO budgetDTO) {
        return new ResponseEntity<>(budgetService.createBudget(budgetDTO), HttpStatus.CREATED);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BudgetDTO>> getBudgetsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(budgetService.getBudgetsByUserId(userId));
    }
}
