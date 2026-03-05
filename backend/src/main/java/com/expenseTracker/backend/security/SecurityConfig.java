package com.expenseTracker.backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    // 1. GÜVENLİK FİLTRESİ ZİNCİRİ (Ana Kurallar)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // CSRF'i kapatıyoruz çünkü token (JWT) kullanacağız, cookie değil.
            .csrf(AbstractHttpConfigurer::disable) 
            
            // İstek izinlerini ayarlıyoruz
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll() // /api/auth/register ve /api/auth/login herkese açık!
                .anyRequest().authenticated() // Diğer TÜM kapılar kilitli (Token isteyecek)
            )
            
            // Oturum (Session) yönetimini STATELESS yapıyoruz. (Yani sunucu kimseyi hatırlamayacak, her istekte token soracak)
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // Bizim yazdığımız JWT Bekçisini, Spring'in standart şifre bekçisinden ÖNCE kapıya koyuyoruz
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // 2. ŞİFRE KIRICI (Hashleme Mekanizması)
    // Veritabanına şifreleri "123456" yerine "$2a$10$X7..." gibi karmaşık bir metin olarak kaydeder.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 3. KİMLİK DOĞRULAYICI (AuthenticationManager)
    // Login olurken kullanıcının girdiği şifre ile veritabanındaki hashlenmiş şifreyi kıyaslayan motor.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
