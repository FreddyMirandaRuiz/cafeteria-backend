package com.cafeteria.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Entity
@Table(name = "producto")
@Data
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false)
    private Double precio;

    @Column(nullable = false)
    @Min(value = 0, message = "El stock no puede ser menor a cero")
    private Integer stock;

    @Column(length = 50)
    private String categoria;

    @Column(length = 255)
    private String descripcion;
    
    @Column(length = 255)
    private String imagen;
}