package com.expenseTracker.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.expenseTracker.backend.dto.SavingGoalDTO;
import com.expenseTracker.backend.service.SavingGoalService;

import java.util.List;

@RestController
@RequestMapping("/api/saving-goals")
@CrossOrigin(origins = "http://localhost:3000")
public class SavingGoalController {

    private final SavingGoalService savingGoalService;

    public SavingGoalController(SavingGoalService savingGoalService) {
        this.savingGoalService = savingGoalService;
    }

    @PostMapping
    public ResponseEntity<SavingGoalDTO> createSavingGoal(@RequestBody SavingGoalDTO dto) {
        return new ResponseEntity<>(savingGoalService.createSavingGoal(dto), HttpStatus.CREATED);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SavingGoalDTO>> getSavingGoalsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(savingGoalService.getSavingGoalsByUserId(userId));
    }
}