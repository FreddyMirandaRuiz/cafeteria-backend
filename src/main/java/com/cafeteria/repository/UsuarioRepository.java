package com.cafeteria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cafeteria.model.Usuario;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsuarioAndPassword(String usuario, String password);
}