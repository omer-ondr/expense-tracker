package com.expenseTracker.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import com.expenseTracker.backend.dto.CategoryDTO;
import com.expenseTracker.backend.entity.Category;
import com.expenseTracker.backend.entity.User;

import org.springframework.stereotype.Service;

import com.expenseTracker.backend.repository.CategoryRepository;
import com.expenseTracker.backend.repository.UserRepository;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public CategoryService(CategoryRepository categoryRepository, UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    // DTO alıp Entity kaydeden metod
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setName(categoryDTO.getName());
        category.setIcon(categoryDTO.getIcon());
        category.setColor(categoryDTO.getColor());
        
        User user = userRepository.findById(categoryDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
        category.setUser(user);

        Category savedCategory = categoryRepository.save(category);
        return convertToDTO(savedCategory);
    }

    // Entity listesini DTO listesine çevirip döndüren metod
    public List<CategoryDTO> getCategoriesByUserId(Long userId) {
        return categoryRepository.findByUserId(userId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Helper Metod: Entity -> DTO
    private CategoryDTO convertToDTO(Category category) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setIcon(category.getIcon());
        dto.setColor(category.getColor());
        dto.setUserId(category.getUser().getId());
        return dto;
    }
}
