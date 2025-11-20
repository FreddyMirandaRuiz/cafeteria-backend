package com.cafeteria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cafeteria.model.Cliente;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByEmail(String email);
}
