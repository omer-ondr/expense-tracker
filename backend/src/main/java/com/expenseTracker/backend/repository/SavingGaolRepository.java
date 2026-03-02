package com.expenseTracker.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.expenseTracker.backend.entity.SavingGoal;

@Repository
public interface SavingGaolRepository extends JpaRepository<SavingGoal, Long> {

    List<SavingGoal> findByUserId(Long userId);
}
