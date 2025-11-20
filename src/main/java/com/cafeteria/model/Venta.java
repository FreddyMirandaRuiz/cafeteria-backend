package com.cafeteria.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cliente;
    private String tipoComprobante;      // "boleta" o "factura"
    private String numeroComprobante;    // Número de comprobante
    private LocalDateTime fecha;
    private Double subtotal;
    private Double igv;
    private Double total;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    private List<DetalleVenta> detalles;
}