package com.cafeteria.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mesa;
    private String productos; // Se puede mantener por compatibilidad o eliminar si no lo usas
    private int cantidad;
    private double total;
    private String estado;
    private String mozo;
    private LocalDateTime fecha;

    // 🔗 Relación con DetallePedido
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JsonManagedReference
    private List<DetallePedido> detalles = new ArrayList<>();

    // 🔹 Constructor
    public Pedido() {
        this.fecha = LocalDateTime.now();
        this.estado = "pendiente";
    }

    // 🔹 Antes de guardar, garantizar valores por defecto
    @PrePersist
    public void prePersist() {
        if (this.fecha == null) this.fecha = LocalDateTime.now();
        if (this.estado == null || this.estado.trim().isEmpty()) this.estado = "pendiente";
    }

    // 🔹 Método para agregar detalles correctamente
    public void agregarDetalle(DetallePedido detalle) {
        detalle.setPedido(this); // vincula ambos lados
        this.detalles.add(detalle);
    }

    // ====== Getters y Setters ======
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMesa() { return mesa; }
    public void setMesa(String mesa) { this.mesa = mesa; }

    public String getProductos() { return productos; }
    public void setProductos(String productos) { this.productos = productos; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getMozo() { return mozo; }
    public void setMozo(String mozo) { this.mozo = mozo; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public List<DetallePedido> getDetalles() { return detalles; }
    public void setDetalles(List<DetallePedido> detalles) {
        this.detalles.clear();
        if (detalles != null) {
            for (DetallePedido detalle : detalles) {
                agregarDetalle(detalle);
            }
        }
    }
}