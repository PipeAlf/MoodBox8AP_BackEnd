package com.MoodBox8ap.Backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.*;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

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

        // Ignorar el login: no aplicar validación de token
        String path = request.getRequestURI();

        if (path.equals("/api/usuarios/login") ||
                (request.getMethod().equals("POST") && path.equals("/api/usuarios"))) {
            System.out.println("🟢 Permitir sin token: " + path);
            filterChain.doFilter(request, response);
            return;
        }


        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            if (jwtUtil.validateToken(token)) {
                String username = jwtUtil.getUsernameFromToken(token);
                String role = jwtUtil.getRoleFromToken(token);
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);
                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(username, null, List.of(authority));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    System.out.println("🔐 Usuario autenticado desde JWT: " + username);
                    System.out.println("🔐 Rol del token: " + role);
                    System.out.println("🔐 Authorities: " + auth.getAuthorities());
                }
            }
        }

        filterChain.doFilter(request, response);

    }

}
