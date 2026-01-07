package com.cafeteria.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "detalle_pedidos") // Aseguramos plural para consistencia
public class DetallePedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombreProducto;
    private int cantidad;
    private double precio;
    private double subtotal;

    // 🔗 Relación inversa al pedido
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id")
    @JsonBackReference // Evita que Jackson entre en un bucle infinito al serializar
    private Pedido pedido;

    // 🔹 Constructor vacío (Requisito de JPA)
    public DetallePedido() {
    }

    // 🔹 Constructor para facilitar la creación desde el Service (Opcional)
    public DetallePedido(String nombreProducto, int cantidad, double precio) {
        this.nombreProducto = nombreProducto;
        this.cantidad = cantidad;
        this.precio = precio;
        this.subtotal = cantidad * precio;
    }

    // 🔹 Lógica automática de cálculo
    @PrePersist
    @PreUpdate
    public void calcularSubtotal() {
        this.subtotal = Math.round((this.cantidad * this.precio) * 100.0) / 100.0;
    }

    // ====== Getters y Setters ======
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }

    public Pedido getPedido() { return pedido; }
    public void setPedido(Pedido pedido) { this.pedido = pedido; }
}