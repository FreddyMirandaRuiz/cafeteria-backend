package com.cafeteria.repository;

import com.cafeteria.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {
	//long countByTipoComprobante(String tipoComprobante);
}