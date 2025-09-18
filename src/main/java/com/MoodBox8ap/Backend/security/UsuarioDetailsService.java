package com.MoodBox8ap.Backend.security;

import com.MoodBox8ap.Backend.model.Usuario;
import com.MoodBox8ap.Backend.service.IUsuarioService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    private final IUsuarioService usuarioService;

    public UsuarioDetailsService(IUsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        System.out.println("🔍 Buscando usuario con correo: " + correo);
        Usuario usuario = usuarioService.buscarPorCorreo(correo);
        if (usuario == null) {
            System.out.println("❌ Usuario no encontrado: " + correo);
            throw new UsernameNotFoundException("Usuario no encontrado: " + correo);
        }
        System.out.println("✅ Usuario encontrado: " + usuario.getCorreo() + " - Rol: " + usuario.getRol());
        
        // mapear rol a GrantedAuthority
        String role = usuario.getRol() != null ? usuario.getRol().name().toUpperCase() : "CLIENTE";
        return new User(
                usuario.getCorreo(),
                usuario.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role))
        );
    }
}
