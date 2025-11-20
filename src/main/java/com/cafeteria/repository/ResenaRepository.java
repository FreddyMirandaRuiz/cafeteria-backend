package com.cafeteria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cafeteria.model.Resena;
import java.util.List;

public interface ResenaRepository extends JpaRepository<Resena, Long> {
    List<Resena> findByProductoId(Long productoId);
}