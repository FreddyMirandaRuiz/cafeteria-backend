package com.cafeteria.service;

import com.cafeteria.model.Venta;
import java.util.List;

public interface VentaService {
	List<Venta> listar();
    Venta registrarVenta(Venta venta);
    
}