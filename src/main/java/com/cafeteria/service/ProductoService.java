package com.cafeteria.service;

import com.cafeteria.model.Producto;
import com.cafeteria.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProductoService {
    private final ProductoRepository repo;

    public ProductoService(ProductoRepository repo) {
        this.repo = repo;
    }

    public List<Producto> listar() { return repo.findAll(); }

    public Producto guardar(Producto p) { return repo.save(p); }

    public void eliminar(Long id) { repo.deleteById(id); }

    public Producto obtener(Long id) {
        return repo.findById(id).orElse(null);
    }
    
// --- NUEVO MÉTODO PARA VALIDACIÓN DE STOCK ---
    
    @Transactional
    public void descontarStock(Long id, Integer cantidad) {
        Producto p = repo.findById(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));

        if (p.getStock() < cantidad) {
            throw new RuntimeException("Stock insuficiente para: " + p.getNombre() + 
                                       " (Disponible: " + p.getStock() + ")");
        }

        p.setStock(p.getStock() - cantidad);
        repo.save(p);
    }
}