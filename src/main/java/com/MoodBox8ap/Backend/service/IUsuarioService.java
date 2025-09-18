package com.MoodBox8ap.Backend.service;

import com.MoodBox8ap.Backend.model.Usuario;
import java.util.List;
import java.util.Optional;

public interface IUsuarioService {
    List<Usuario> listarUsuarios();
    Optional<Usuario> obtenerPorId(Long id);
    Usuario guardarUsuario(Usuario usuario);
    void eliminarUsuario(Long id);
    Optional<Usuario> buscarPorCorreo(String correo);
}
