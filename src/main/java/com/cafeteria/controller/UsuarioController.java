package com.cafeteria.controller;

import com.cafeteria.model.Usuario;
import com.cafeteria.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "http://localhost:4200")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;

    public UsuarioController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping
    public ResponseEntity<?> listar() {
        return ResponseEntity.ok(usuarioRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<?> registrar(@RequestBody Usuario usuario) {
        return ResponseEntity.ok(usuarioRepository.save(usuario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        usuarioRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    // ✅ Nuevo método: actualizar  usuario
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizar(@PathVariable Long id, @RequestBody Usuario datos) {
        return usuarioRepository.findById(id)
            .map(usuario -> {
                usuario.setNombre(datos.getNombre());
                usuario.setUsuario(datos.getUsuario());
                usuario.setPassword(datos.getPassword());
                usuario.setRol(datos.getRol());
                usuarioRepository.save(usuario);
                return ResponseEntity.ok(usuario);
            })
            .orElse(ResponseEntity.notFound().build());
    }
   
}