package com.cafeteria.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cafeteria.model.Resena;
import com.cafeteria.repository.ResenaRepository;
import java.util.List;

@Service
public class ResenaService {

    @Autowired
    private ResenaRepository repo;

    public List<Resena> listarPorProducto(Long productoId) {
        return repo.findByProductoId(productoId);
    }

    public Resena guardar(Resena resena) {
        return repo.save(resena);
    }
}