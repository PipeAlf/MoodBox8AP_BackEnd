package com.MoodBox8ap.Backend.config;

import com.MoodBox8ap.Backend.model.Rol;
import com.MoodBox8ap.Backend.model.Usuario;
import com.MoodBox8ap.Backend.repository.IUsuarioRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AdminInitializer {

    private final IUsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminInitializer(IUsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void crearAdminSiNoExiste() {
        String correoAdmin = "admin@moodbox.com";
        Optional<Usuario> existente = usuarioRepository.findByCorreo(correoAdmin);
        if (existente.isEmpty()) {
            Usuario admin = new Usuario();
            admin.setCorreo(correoAdmin);
            admin.setNombre("Administrador");
            admin.setApellido("MoodBox");
            admin.setRol(Rol.ADMIN);
            admin.setTelefono("000000000");
            admin.setPassword(passwordEncoder.encode("admin123"));
            usuarioRepository.save(admin);
            System.out.println("Admin creado correctamente en la base de datos.");
        }
        System.out.println("Admin ya existe. No se creó uno nuevo.");
    }
}
