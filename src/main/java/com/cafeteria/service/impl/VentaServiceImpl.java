package com.cafeteria.service.impl;

import com.cafeteria.model.DetalleVenta;
import com.cafeteria.model.Producto;
import com.cafeteria.model.Venta;
import com.cafeteria.repository.ProductoRepository;
import com.cafeteria.repository.VentaRepository;
import com.cafeteria.service.VentaService;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class VentaServiceImpl implements VentaService {

    private final VentaRepository ventaRepo;
    private final ProductoRepository productoRepo;

    public VentaServiceImpl(VentaRepository ventaRepo, ProductoRepository productoRepo) {
        this.ventaRepo = ventaRepo;
        this.productoRepo = productoRepo;
    }

    @Override
    public List<Venta> listar() {
        return ventaRepo.findAll();
    }

    @Override
    public Venta registrarVenta(Venta venta) {

        venta.setFecha(LocalDateTime.now());
        venta.setNumeroComprobante(generarNumeroComprobante(venta.getTipoComprobante()));

        BigDecimal subtotal = BigDecimal.ZERO;

        for (DetalleVenta det : venta.getDetalles()) {

            Producto prod = productoRepo.findById(det.getProducto().getId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            det.setVenta(venta);

            BigDecimal precio = BigDecimal.valueOf(prod.getPrecio());
            BigDecimal cantidad = BigDecimal.valueOf(det.getCantidad());
            BigDecimal sub = precio.multiply(cantidad);

            det.setSubtotal(sub.doubleValue());
            subtotal = subtotal.add(sub);
        }

        BigDecimal igv = subtotal.multiply(BigDecimal.valueOf(0.18))
                .setScale(2, BigDecimal.ROUND_HALF_UP);

        BigDecimal total = subtotal.add(igv)
                .setScale(2, BigDecimal.ROUND_HALF_UP);

        venta.setSubtotal(subtotal.setScale(2).doubleValue());
        venta.setIgv(igv.doubleValue());
        venta.setTotal(total.doubleValue());

        return ventaRepo.save(venta);
    }

    private String generarNumeroComprobante(String tipo) {
        String prefijo = tipo.equalsIgnoreCase("factura") ? "F001" : "B001";
        long numero = ventaRepo.count() + 1;
        return prefijo + String.format("%06d", numero);
    }
}