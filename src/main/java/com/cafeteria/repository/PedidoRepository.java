package com.cafeteria.repository;


import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.query.Param;
import com.cafeteria.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
	//List<Pedido> findByEstado(String estado);
    // Métodos personalizados si es necesario
	@Modifying
    @Transactional
    @Query("UPDATE Pedido p SET p.estado = :estado WHERE p.id = :id")
    int actualizarEstado(@Param("id") Long id, @Param("estado") String estado);
}