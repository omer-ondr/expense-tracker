package com.expenseTracker.backend.service;

import org.springframework.stereotype.Service;

import com.expenseTracker.backend.dto.SavingGoalDTO;
import com.expenseTracker.backend.entity.SavingGoal;
import com.expenseTracker.backend.entity.User;
import com.expenseTracker.backend.repository.SavingGoalRepository;
import com.expenseTracker.backend.repository.UserRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SavingGoalService {

    private final SavingGoalRepository savingGoalRepository;
    private final UserRepository userRepository;

    public SavingGoalService(SavingGoalRepository savingGoalRepository, UserRepository userRepository) {
        this.savingGoalRepository = savingGoalRepository;
        this.userRepository = userRepository;
    }

    public SavingGoalDTO createSavingGoal(SavingGoalDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı!"));

        SavingGoal goal = new SavingGoal();
        goal.setGoalName(dto.getGoalName());
        goal.setTargetAmount(dto.getTargetAmount());
        
        // Yeni hedef açıldığında, eğer frontend'den bir miktar gelmediyse 0'dan başlar
        goal.setCurrentAmount(dto.getCurrentAmount() != null ? dto.getCurrentAmount() : BigDecimal.ZERO);
        goal.setDeadline(dto.getDeadline());
        goal.setUser(user);

        SavingGoal savedGoal = savingGoalRepository.save(goal);
        return convertToDTO(savedGoal);
    }

    public List<SavingGoalDTO> getSavingGoalsByUserId(Long userId) {
        return savingGoalRepository.findByUserId(userId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private SavingGoalDTO convertToDTO(SavingGoal goal) {
        SavingGoalDTO dto = new SavingGoalDTO();
        dto.setId(goal.getId());
        dto.setGoalName(goal.getGoalName());
        dto.setTargetAmount(goal.getTargetAmount());
        dto.setCurrentAmount(goal.getCurrentAmount());
        dto.setDeadline(goal.getDeadline());
        dto.setUserId(goal.getUser().getId());
        return dto;
    }
}
