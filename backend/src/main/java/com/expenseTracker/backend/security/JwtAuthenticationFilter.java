package com.expenseTracker.backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, CustomUserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. İstekten (Request) "Authorization" başlığını al
        final String authHeader = request.getHeader("Authorization");
        final String userEmail;
        final String jwtToken;

        // 2. Başlık boşsa veya "Bearer " ile başlamıyorsa, engelleme ama işleme de alma (Belki giriş/kayıt isteğidir)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. "Bearer " kelimesinden sonrasını (asıl tokenı) kesip al
        jwtToken = authHeader.substring(7);
        userEmail = jwtUtil.extractEmail(jwtToken);

        // 4. Token'da e-posta varsa ve kullanıcı henüz sisteme giriş yapmamışsa (SecurityContext boşsa)
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            
            // Veritabanından kullanıcıyı getir
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            // 5. Token geçerliyse, kullanıcıya "Geçiş İzni" (Authentication) ver
            if (jwtUtil.validateToken(jwtToken, userDetails.getUsername())) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
                // Güvenlik bağlamına (Context) kaydet ki diğer API'ler kullanıcının kim olduğunu bilsin
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        
        // İşleme devam etmesine izin ver
        filterChain.doFilter(request, response);
    }
}
