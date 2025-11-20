package com.cafeteria.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.cafeteria.model.Resena;
import com.cafeteria.service.ResenaService;
import java.util.List;

@RestController
@RequestMapping("/api/resenas")
@CrossOrigin(origins = "http://localhost:4200")
public class ResenaController {

    @Autowired
    private ResenaService service;

    @GetMapping("/producto/{id}")
    public List<Resena> listarPorProducto(@PathVariable Long id) {
        return service.listarPorProducto(id);
    }

    @PostMapping
    public Resena guardar(@RequestBody Resena resena) {
        return service.guardar(resena);
    }
}
