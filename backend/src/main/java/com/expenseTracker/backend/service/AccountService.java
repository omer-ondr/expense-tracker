package com.expenseTracker.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.expenseTracker.backend.entity.Account;
import com.expenseTracker.backend.repository.AccountRepository;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    // Yeni bir hesap/cüzdan oluştur
    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    // Sadece o anki kullanıcının hesaplarını getir
    public List<Account> getAccountsByUserId(Long userId) {
        return accountRepository.findByUserId(userId);
    }
}
