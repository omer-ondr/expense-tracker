package com.expenseTracker.backend.service;

import org.springframework.stereotype.Service;

import com.expenseTracker.backend.dto.BudgetDTO;
import com.expenseTracker.backend.entity.Budget;
import com.expenseTracker.backend.entity.Category;
import com.expenseTracker.backend.entity.User;
import com.expenseTracker.backend.repository.BudgetRepository;
import com.expenseTracker.backend.repository.CategoryRepository;
import com.expenseTracker.backend.repository.UserRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public BudgetService(BudgetRepository budgetRepository, CategoryRepository categoryRepository, UserRepository userRepository) {
        this.budgetRepository = budgetRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    public BudgetDTO createBudget(BudgetDTO dto) {
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Kategori bulunamadı!"));
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı!"));

        Budget budget = new Budget();
        budget.setAmountLimit(dto.getAmountLimit());
        // Yeni bütçe oluşturulurken harcanan miktar her zaman 0'dan başlar
        budget.setSpentAmount(BigDecimal.ZERO); 
        budget.setStartDate(dto.getStartDate());
        budget.setEndDate(dto.getEndDate());
        budget.setCategory(category);
        budget.setUser(user);

        Budget savedBudget = budgetRepository.save(budget);
        return convertToDTO(savedBudget);
    }

    public List<BudgetDTO> getBudgetsByUserId(Long userId) {
        return budgetRepository.findByUserId(userId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Entity'den DTO'ya dönüştürücü
    private BudgetDTO convertToDTO(Budget budget) {
        BudgetDTO dto = new BudgetDTO();
        dto.setId(budget.getId());
        dto.setAmountLimit(budget.getAmountLimit());
        dto.setSpentAmount(budget.getSpentAmount());
        dto.setStartDate(budget.getStartDate());
        dto.setEndDate(budget.getEndDate());
        dto.setCategoryId(budget.getCategory().getId());
        dto.setCategoryName(budget.getCategory().getName());
        dto.setUserId(budget.getUser().getId());
        return dto;
    }
}
