package com.expenseTracker.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.expenseTracker.backend.dto.AccountDTO;
import com.expenseTracker.backend.entity.Account;
import com.expenseTracker.backend.entity.User;
import com.expenseTracker.backend.repository.AccountRepository;
import com.expenseTracker.backend.repository.UserRepository;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountService(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    public AccountDTO createAccount(AccountDTO dto) {
        Account account = new Account();
        account.setAccountName(dto.getAccountName());
        account.setAccountType(dto.getAccountType());
        account.setBalance(dto.getBalance());

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
        account.setUser(user);

        Account saved = accountRepository.save(account);
        return convertToDTO(saved);
    }

    public List<AccountDTO> getAccountsByUserId(Long userId) {
        return accountRepository.findByUserId(userId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private AccountDTO convertToDTO(Account account) {
        AccountDTO dto = new AccountDTO();
        dto.setId(account.getId());
        dto.setAccountName(account.getAccountName());
        dto.setAccountType(account.getAccountType());
        dto.setBalance(account.getBalance());
        dto.setUserId(account.getUser().getId());
        return dto;
    }
}
