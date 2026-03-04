package com.expenseTracker.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String passwordHash; // Şifreyi açık tutmuyoruz

    @Column(nullable = false)
    private String defaultCurrency = "TRY"; // Kullanıcının varsayılan para birimi

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // İpucu: İleride Account ve Category listelerini buraya OneToMany olarak
    // bağlayacağız,
    // ancak çift yönlü (bidirectional) ilişki kurarken sonsuz döngüye girmemek için
    // şimdilik sade bırakıyoruz.
    // KULLANICININ HESAPLARI
    // cascade = CascadeType.ALL -> Kullanıcı silinirse, hesapları da silinsin.
    // orphanRemoval = true -> Hesabın kullanıcıyla bağı koparsa veritabanından
    // silinsin.
    @JsonIgnore // Sonsuz döngüyü engellemek için çok kritik!
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Account> accounts = new ArrayList<>();

    // KULLANICININ KATEGORİLERİ
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Category> categories = new ArrayList<>();

    // KULLANICININ İŞLEMLERİ (TRANSACTIONS)
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions = new ArrayList<>();
}