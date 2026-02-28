package com.expenseTracker.backend.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.expenseTracker.backend.entity.enums.TransactionType;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(length = 255)
    private String description; // Örn: "Aylık Netflix Aboneliği"

    @Column(nullable = false)
    private LocalDateTime transactionDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType transactionType; // INCOME, EXPENSE veya TRANSFER

    // Bu işlem hangi hesaptan/cüzdandan yapıldı?
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    // Bu işlem hangi kategoriye ait? (Market, Maaş vb.)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    // Güvenlik ve sorgu kolaylığı için işlemi doğrudan kullanıcıya da bağlıyoruz
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
