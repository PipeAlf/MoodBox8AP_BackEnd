package com.MoodBox8ap.Backend.service;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.MoodBox8ap.Backend.model.Usuario;
import com.MoodBox8ap.Backend.repository.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService implements IUsuarioService {

    private final IUsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioService(IUsuarioRepository usuarioRepository,
                          @Lazy PasswordEncoder passwordEncoder) { // 👈 con @Lazy rompemos el ciclo
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    @Override
    public Optional<Usuario> obtenerPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public Usuario guardarUsuario(Usuario usuario) {
        if (usuario.getPassword() != null) {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }
        return usuarioRepository.save(usuario);
    }

    @Override
    public void eliminarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }

    @Override
    public Usuario buscarPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }
}
