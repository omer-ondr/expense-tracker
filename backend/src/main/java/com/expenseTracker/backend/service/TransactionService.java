package com.expenseTracker.backend.service;

import org.springframework.stereotype.Service;

import com.expenseTracker.backend.entity.Account;
import com.expenseTracker.backend.entity.Transaction;
import com.expenseTracker.backend.entity.enums.TransactionType;
import com.expenseTracker.backend.repository.AccountRepository;
import com.expenseTracker.backend.repository.TransactionRepository;

import jakarta.transaction.Transactional;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional
    public Transaction createTransaction(Transaction transaction) {
        
        // 1. İşlemin yapılacağı hesabı veritabanından bul
        Account account = accountRepository.findById(transaction.getAccount().getId())
                .orElseThrow(() -> new RuntimeException("Hesap bulunamadı!"));

        // 2. İşlem tipine göre bakiyeyi güncelle (Matematiksel işlemler)
        if (transaction.getTransactionType() == TransactionType.EXPENSE) {
            // Gider ise bakiyeden düş (BigDecimal kullanıyoruz)
            account.setBalance(account.getBalance().subtract(transaction.getAmount()));
        } else if (transaction.getTransactionType() == TransactionType.INCOME) {
            // Gelir ise bakiyeye ekle
            account.setBalance(account.getBalance().add(transaction.getAmount()));
        }

        // 3. Güncel bakiyeli hesabı kaydet
        accountRepository.save(account);

        // 4. Son olarak işlemin (transaction) kendisini kaydet ve döndür
        return transactionRepository.save(transaction);
    }

}
