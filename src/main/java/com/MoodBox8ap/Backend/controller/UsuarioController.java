package com.MoodBox8ap.Backend.controller;

import com.MoodBox8ap.Backend.dto.LoginRequest;
import com.MoodBox8ap.Backend.dto.LoginResponse;
import com.MoodBox8ap.Backend.model.Usuario;
import com.MoodBox8ap.Backend.service.IUsuarioService;
import com.MoodBox8ap.Backend.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final IUsuarioService usuarioService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public UsuarioController(IUsuarioService usuarioService,
                             AuthenticationManager authenticationManager,
                             JwtUtil jwtUtil) {
        this.usuarioService = usuarioService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    // Crear usuario
    @PostMapping
    public ResponseEntity<Usuario> guardarUsuario(@RequestBody Usuario usuario) {
        Usuario saved = usuarioService.guardarUsuario(usuario);
        saved.setPassword(null); // Evitamos enviar la contraseña al front
        return ResponseEntity.ok(saved);
    }

    // Login con JWT
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getCorreo(),
                            loginRequest.getPassword()
                    )
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtUtil.generateToken(userDetails.getUsername());

            Usuario usuario = usuarioService.buscarPorCorreo(userDetails.getUsername());
            usuario.setPassword(null);

            return ResponseEntity.ok(new LoginResponse(token, usuario));

        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(401).body("Credenciales incorrectas");
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
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        return usuarioService.obtenerPorId(id)
                .map(u -> {
                    usuario.setIdUsuario(id);
                    return ResponseEntity.ok(usuarioService.guardarUsuario(usuario));
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
