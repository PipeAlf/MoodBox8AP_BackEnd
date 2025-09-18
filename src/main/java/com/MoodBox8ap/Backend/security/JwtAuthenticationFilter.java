package com.MoodBox8ap.Backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.*;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        System.out.println("🔍 JWT Filter - Header Authorization: " + header);
        
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            System.out.println("🔍 JWT Filter - Token: " + token.substring(0, Math.min(20, token.length())) + "...");
            
            if (jwtUtil.validateToken(token)) {
                String username = jwtUtil.getUsernameFromToken(token);
                System.out.println("🔍 JWT Filter - Username from token: " + username);
                
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    try {
                        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                        UsernamePasswordAuthenticationToken auth =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(auth);
                        System.out.println("✅ JWT Filter - Authentication set for: " + username);
                    } catch (Exception e) {
                        System.out.println("❌ JWT Filter - Error loading user: " + e.getMessage());
                    }
                }
            } else {
                System.out.println("❌ JWT Filter - Invalid token");
            }
        } else {
            System.out.println("❌ JWT Filter - No Bearer token found");
        }
        filterChain.doFilter(request, response);
    }
}
