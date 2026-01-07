package com.cafeteria.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.cafeteria.model.Resena;
import com.cafeteria.model.Producto;//extra
import com.cafeteria.service.ResenaService;
import java.util.List;
import com.cafeteria.repository.ProductoRepository;//extra

@RestController
@RequestMapping("/api/resenas")
@CrossOrigin(origins = "http://localhost:4200")
public class ResenaController {
	
	@Autowired//EXTRA
    private ProductoRepository productoRepo;//Extra

    @Autowired
    private ResenaService service;

    @GetMapping("/producto/{id}")
    public List<Resena> listarPorProducto(@PathVariable Long id) {
        return service.listarPorProducto(id);
    }

    @PostMapping
    public Resena guardar(@RequestBody Resena resena) {
    	// 👉 Cargar producto completo ANTES de guardar
        Producto producto = productoRepo.findById(resena.getProducto().getId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        resena.setProducto(producto);
        return service.guardar(resena);
    }
}
