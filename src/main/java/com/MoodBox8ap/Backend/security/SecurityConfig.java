package com.MoodBox8ap.Backend.security;

import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SecurityConfig {

    private final UsuarioDetailsService usuarioDetailsService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(
            UsuarioDetailsService usuarioDetailsService,
            JwtUtil jwtUtil,
            PasswordEncoder passwordEncoder
    ) {
        this.usuarioDetailsService = usuarioDetailsService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtUtil, usuarioDetailsService);
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(usuarioDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(provider);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> {})
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/usuarios/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/usuarios").permitAll()

                        // ventas
                        .requestMatchers(HttpMethod.POST, "/api/ventas").hasRole("CLIENTE")
                        .requestMatchers(HttpMethod.GET, "/api/ventas").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/ventas/**").hasRole("ADMIN")



                        // GET públicos para catálogo
                        .requestMatchers(HttpMethod.GET, "/api/productos").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/productos/activos").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/productos/{id}").permitAll()

                        // ADMIN puede gestionar productos
                        .requestMatchers(HttpMethod.POST, "/api/productos").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/productos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/productos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/productos/**").hasRole("ADMIN")

                        // CLIENTE puede acceder a su carrito
                        .requestMatchers("/api/carrito/**").hasAnyRole("CLIENTE", "ADMIN")

                        // Cliente o admin puede editar perfil
                        .requestMatchers(HttpMethod.PUT, "/api/usuarios/**").hasAnyRole("CLIENTE", "ADMIN")

                        .anyRequest().authenticated()
                )

                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationManager(authenticationManager());

        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOriginPatterns("https://main.drkoft4my5rgd.amplifyapp.com")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }


}

