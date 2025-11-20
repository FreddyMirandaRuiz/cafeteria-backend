package com.cafeteria.service;

import com.cafeteria.model.Cliente;
import com.cafeteria.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository repo;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Cliente registrar(Cliente cliente) {
        cliente.setPassword(passwordEncoder.encode(cliente.getPassword()));
        return repo.save(cliente);
    }

    public Optional<Cliente> buscarPorEmail(String email) {
        return repo.findByEmail(email);
    }

    public boolean validarPassword(String passwordPlano, String passwordEncriptado) {
        return passwordEncoder.matches(passwordPlano, passwordEncriptado);
    }
}