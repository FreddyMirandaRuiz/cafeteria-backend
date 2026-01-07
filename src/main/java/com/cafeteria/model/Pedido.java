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
    
    @Column(length = 1000) // Aumentamos tamaño para el resumen de muchos productos
    private String productos; 

    private int cantidad;
    private double total;
    private String estado;
    private String mozo;
    private LocalDateTime fecha;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JsonManagedReference
    private List<DetallePedido> detalles = new ArrayList<>();

    public Pedido() {
        this.fecha = LocalDateTime.now();
        this.estado = "pendiente";
    }

    @PrePersist
    public void prePersist() {
        if (this.fecha == null) this.fecha = LocalDateTime.now();
        if (this.estado == null || this.estado.trim().isEmpty()) this.estado = "pendiente";
        
        // Sincronizar datos antes de insertar en la BD
        actualizarResumenYTotales();
    }

    @PreUpdate
    public void preUpdate() {
        // Recalcular si se edita el pedido
        actualizarResumenYTotales();
    }

    /**
     * ✨ MEJORA: Sincroniza el string resumen, la cantidad total y el monto total
     * basado en la lista de detalles (el carrito).
     */
    public void actualizarResumenYTotales() {
        if (this.detalles == null || this.detalles.isEmpty()) return;

        StringBuilder resumen = new StringBuilder();
        double sumaTotal = 0;
        int sumaCantidad = 0;

        for (DetallePedido d : detalles) {
            resumen.append(d.getCantidad())
                   .append(" ")
                   .append(d.getNombreProducto())
                   .append(", ");
            
            // Validamos subtotal: cantidad * precio
            double sub = d.getCantidad() * d.getPrecio();
            d.setSubtotal(sub);
            
            sumaTotal += sub;
            sumaCantidad += d.getCantidad();
        }

        // Limpiar la última coma y espacio
        String res = resumen.toString();
        this.productos = res.isEmpty() ? "" : res.substring(0, res.length() - 2);
        this.total = Math.round(sumaTotal * 100.0) / 100.0; // Redondeo a 2 decimales
        this.cantidad = sumaCantidad;
    }

    public void agregarDetalle(DetallePedido detalle) {
        if (detalle != null) {
            detalle.setPedido(this);
            this.detalles.add(detalle);
        }
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
            detalles.forEach(this::agregarDetalle);
        }
    }
}