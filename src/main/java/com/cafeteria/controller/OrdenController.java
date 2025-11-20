package com.cafeteria.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.cafeteria.model.Orden;
import com.cafeteria.model.DetalleOrden;
import com.cafeteria.repository.OrdenRepository;
import java.util.List;

@RestController
@RequestMapping("/api/ordenes")
@CrossOrigin(origins = "http://localhost:4200")
public class OrdenController {

    @Autowired
    private OrdenRepository repo;

    @PostMapping
    public Orden registrarOrden(@RequestBody Orden orden) {
    	if (orden.getDetalles() != null) {
            for (DetalleOrden d : orden.getDetalles()) {
                d.setOrden(orden);
            }
        }
        return repo.save(orden);
    }
    //agregamos el extra
    @GetMapping
    public List<Orden> listarPorCliente(@RequestParam(required = false) String cliente) {
        if (cliente != null) {
            return repo.findByCliente(cliente);
        }
        return repo.findAll();
    }
}