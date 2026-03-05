package com.expenseTracker.backend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.expenseTracker.backend.dto.TransactionDTO;
import com.expenseTracker.backend.entity.Account;
import com.expenseTracker.backend.entity.Category;
import com.expenseTracker.backend.entity.Transaction;
import com.expenseTracker.backend.entity.User;
import com.expenseTracker.backend.entity.enums.TransactionType;
import com.expenseTracker.backend.repository.AccountRepository;
import com.expenseTracker.backend.repository.BudgetRepository;
import com.expenseTracker.backend.repository.CategoryRepository;
import com.expenseTracker.backend.repository.TransactionRepository;
import com.expenseTracker.backend.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final BudgetRepository budgetRepository; // 1. Bütçe Reposu Eklendi

    // Constructor'ı (Yapıcı Metot) Güncelliyoruz
    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository,
            CategoryRepository categoryRepository, UserRepository userRepository,
            BudgetRepository budgetRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.budgetRepository = budgetRepository;
    }

    @Transactional
    public TransactionDTO createTransaction(TransactionDTO dto) {
        Account account = accountRepository.findById(dto.getAccountId())
                .orElseThrow(() -> new RuntimeException("Hesap bulunamadı"));
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Kategori bulunamadı"));
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        Transaction transaction = new Transaction();
        transaction.setAmount(dto.getAmount());
        transaction.setDescription(dto.getDescription());
        transaction.setTransactionDate(dto.getTransactionDate());
        transaction.setTransactionType(dto.getTransactionType());
        transaction.setAccount(account);
        transaction.setCategory(category);
        transaction.setUser(user);

        // --- AKILLI DOKUNUŞ BURADA BAŞLIYOR ---
        if (dto.getTransactionType() == TransactionType.EXPENSE) {
            account.setBalance(account.getBalance().subtract(dto.getAmount()));

            budgetRepository.findByUserIdAndCategoryId(dto.getUserId(), dto.getCategoryId())
                    .ifPresent(budget -> {
                        budget.setSpentAmount(budget.getSpentAmount().add(dto.getAmount()));
                        budgetRepository.save(budget);
                    });

            // 2. GELİR MANTIĞI
        } else if (dto.getTransactionType() == TransactionType.INCOME) {
            account.setBalance(account.getBalance().add(dto.getAmount()));

            // 3. TRANSFER MANTIĞI (YENİ!)
        } else if (dto.getTransactionType() == TransactionType.TRANSFER) {
            // Ana hesaptan (Gönderen) parayı düş
            account.setBalance(account.getBalance().subtract(dto.getAmount()));

            // Hedef hesabı (Alan) bul ve parayı ekle
            if (dto.getTargetAccountId() == null) {
                throw new RuntimeException("Transfer için hedef hesap (targetAccountId) zorunludur!");
            }
            Account targetAccount = accountRepository.findById(dto.getTargetAccountId())
                    .orElseThrow(() -> new RuntimeException("Hedef hesap bulunamadı!"));

            targetAccount.setBalance(targetAccount.getBalance().add(dto.getAmount()));
            accountRepository.save(targetAccount); // Hedef hesabı da kaydet
        }

        accountRepository.save(account);
        Transaction saved = transactionRepository.save(transaction);
        return convertToDTO(saved);
    }

    public List<TransactionDTO> getTransactionsByUserId(Long userId) {
        return transactionRepository.findByUserId(userId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<TransactionDTO> getTransactionsByDate(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        return transactionRepository.findByUserIdAndTransactionDateBetween(userId, startDate, endDate)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private TransactionDTO convertToDTO(Transaction t) {
        TransactionDTO dto = new TransactionDTO();
        dto.setId(t.getId());
        dto.setAmount(t.getAmount());
        dto.setDescription(t.getDescription());
        dto.setTransactionDate(t.getTransactionDate());
        dto.setTransactionType(t.getTransactionType());
        dto.setAccountId(t.getAccount().getId());
        dto.setAccountName(t.getAccount().getAccountName());
        dto.setCategoryId(t.getCategory().getId());
        dto.setCategoryName(t.getCategory().getName());
        dto.setUserId(t.getUser().getId());
        return dto;
    }
}
