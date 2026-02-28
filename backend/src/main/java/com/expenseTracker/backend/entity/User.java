package com.expenseTracker.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

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
    
    // İpucu: İleride Account ve Category listelerini buraya OneToMany olarak bağlayacağız,
    // ancak çift yönlü (bidirectional) ilişki kurarken sonsuz döngüye girmemek için şimdilik sade bırakıyoruz.
}