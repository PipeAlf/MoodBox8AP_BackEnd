package com.MoodBox8ap.Backend.controller;

import com.MoodBox8ap.Backend.dto.LoginRequest;
import com.MoodBox8ap.Backend.dto.LoginResponse;
import com.MoodBox8ap.Backend.model.Rol;
import com.MoodBox8ap.Backend.model.Usuario;
import com.MoodBox8ap.Backend.model.Venta;
import com.MoodBox8ap.Backend.service.IUsuarioService;
import com.MoodBox8ap.Backend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "https://main.drkoft4my5rgd.amplifyapp.com")
public class UsuarioController {

    private final IUsuarioService usuarioService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public UsuarioController(IUsuarioService usuarioService,
                             AuthenticationManager authenticationManager,
                             JwtUtil jwtUtil) {
        this.usuarioService = usuarioService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    // Crear usuario
    @PostMapping
    public ResponseEntity<?> guardarUsuario(@RequestBody Usuario usuario) {
        Optional<Usuario> existente = usuarioService.buscarPorCorreo(usuario.getCorreo());

        if (existente.isPresent()) {
            return ResponseEntity.status(409).body("El correo ya está registrado.");
        }

        usuario.setRol(Rol.CLIENTE); // Forzar rol
        usuario.setEstado("activo"); // Forzar estado
        Usuario saved = usuarioService.guardarUsuario(usuario);
        saved.setPassword(null); // No devolver contraseña
        return ResponseEntity.ok(saved);
    }


    // Login con JWT
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            // Autenticación usando AuthenticationManager (funciona tanto para admin como cliente)
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getCorreo(), loginRequest.getPassword())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Usuario usuario = usuarioService.buscarPorCorreo(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            String token = jwtUtil.generateToken(usuario.getCorreo(), usuario.getRol().name());
            System.out.println("🔐 Password DB: " + usuario.getPassword());
            System.out.println("🔐 Password Login: " + loginRequest.getPassword());
            System.out.println("🔐 Matches: " + passwordEncoder.matches(loginRequest.getPassword(), usuario.getPassword()));

            return ResponseEntity.ok(new LoginResponse(token, usuario));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body("Credenciales inválidas");
        }
    }



    // Listar todos los usuarios
    @GetMapping
    public List<Usuario> listarUsuarios() {
        return usuarioService.listarUsuarios();
    }

    // Obtener usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerUsuario(@PathVariable Long id) {
        return usuarioService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Actualizar usuario
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuarioActualizado) {
        return usuarioService.obtenerPorId(id)
                .map(usuarioExistente -> {
                    usuarioExistente.setNombre(usuarioActualizado.getNombre());
                    usuarioExistente.setApellido(usuarioActualizado.getApellido());
                    usuarioExistente.setCorreo(usuarioActualizado.getCorreo());
                    usuarioExistente.setTelefono(usuarioActualizado.getTelefono());

                    // 👇 SOLO actualiza si hay una nueva password no vacía
                    if (usuarioActualizado.getPassword() != null && !usuarioActualizado.getPassword().isBlank()) {
                        usuarioExistente.setPassword(usuarioActualizado.getPassword());
                    }

                    // 👇 SOLO actualiza si hay una nueva foto no vacía
                    if (usuarioActualizado.getFoto() != null && !usuarioActualizado.getFoto().isBlank()) {
                        usuarioExistente.setFoto(usuarioActualizado.getFoto());
                    }

                    return ResponseEntity.ok(usuarioService.guardarUsuario(usuarioExistente));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }



    // Eliminar usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}
