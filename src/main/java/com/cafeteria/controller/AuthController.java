package com.cafeteria.controller;
import com.cafeteria.model.Usuario;
import com.cafeteria.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario loginRequest) {

        Optional<Usuario> user = usuarioRepository.findByUsuarioAndPassword(
                loginRequest.getUsuario(),
                loginRequest.getPassword()
        );
        
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());   // ✅ Login correcto
        } else {
            return ResponseEntity.status(401).body("Credenciales incorrectas"); 
        }

       
    }
}