package com.expenseTracker.backend.security;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.expenseTracker.backend.dto.AuthResponse;
import com.expenseTracker.backend.dto.LoginRequest;
import com.expenseTracker.backend.dto.RegisterRequest;
import com.expenseTracker.backend.entity.User;
import com.expenseTracker.backend.repository.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, 
                       JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    // 1. KAYIT OLMA (REGISTER) MANTIĞI
    public AuthResponse register(RegisterRequest request) {
        // E-posta daha önce alınmış mı kontrol et
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Bu e-posta adresi zaten kullanımda!");
        }

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        
        // ŞİFREYİ HASHLEYEREK KAYDET (En önemli kısım)
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        
        userRepository.save(user);

        // Kullanıcı kayıt olur olmaz ona bir token verip giriş yapmış sayıyoruz
        String token = jwtUtil.generateToken(user.getEmail());
        return new AuthResponse(token, user.getId(), user.getFirstName(), user.getLastName());
    }

    // 2. GİRİŞ YAPMA (LOGIN) MANTIĞI
    public AuthResponse login(LoginRequest request) {
        // Spring Security bu metod ile kullanıcının şifresini kontrol eder. Yanlışsa hata fırlatır.
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // Şifre doğruysa kullanıcıyı veritabanından çek ve Token üret
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
                
        String token = jwtUtil.generateToken(user.getEmail());
        
        return new AuthResponse(token, user.getId(), user.getFirstName(), user.getLastName());
    }
}
