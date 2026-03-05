package com.expenseTracker.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.expenseTracker.backend.entity.Budget;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {

    List<Budget> findByUserId(Long userId);

    Optional<Budget> findByUserIdAndCategoryId(Long userId, Long categoryId);

}
