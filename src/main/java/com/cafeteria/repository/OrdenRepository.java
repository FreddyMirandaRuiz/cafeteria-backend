package com.cafeteria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cafeteria.model.Orden;
import java.util.List;

public interface OrdenRepository extends JpaRepository<Orden, Long> {
	List<Orden> findByCliente(String cliente);
}