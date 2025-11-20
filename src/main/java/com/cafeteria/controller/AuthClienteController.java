package com.cafeteria.controller;

import com.cafeteria.model.Cliente;
import com.cafeteria.security.JwtUtil;
import com.cafeteria.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.Optional;
import java.util.HashMap;

@RestController
@RequestMapping("/api/auth-cliente")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthClienteController {

    @Autowired
    private ClienteService service;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public Cliente registrar(@RequestBody Cliente cliente) {
        return service.registrar(cliente);
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");

        Optional<Cliente> opt = service.buscarPorEmail(email);
        Map<String, Object> response = new HashMap<>();

        if (opt.isPresent() && service.validarPassword(password, opt.get().getPassword())) {
            String token = jwtUtil.generarToken(email);
            response.put("token", token);
            response.put("nombre", opt.get().getNombre());
            return response;
        } else {
            response.put("error", "Credenciales incorrectas");
            return response;
        }
    }
}