package com.cafeteria.service.impl;

import com.cafeteria.model.DetalleVenta;
import com.cafeteria.model.Producto;
import com.cafeteria.model.Venta;
import com.cafeteria.repository.ProductoRepository;
import com.cafeteria.repository.VentaRepository;
import com.cafeteria.service.VentaService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Importante

import java.math.BigDecimal;
import java.math.RoundingMode;
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
    @Transactional // 🔥 Si un producto no tiene stock, se cancela TODA la venta
    public Venta registrarVenta(Venta venta) {

        venta.setFecha(LocalDateTime.now());
        venta.setNumeroComprobante(generarNumeroComprobante(venta.getTipoComprobante()));

        BigDecimal subtotalAcumulado = BigDecimal.ZERO;

        for (DetalleVenta det : venta.getDetalles()) {

            Producto prod = productoRepo.findById(det.getProducto().getId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            // 🔹 VALIDACIÓN Y DESCUENTO DE STOCK
            if (prod.getStock() < det.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para: " + prod.getNombre() + 
                                           " (Disponible: " + prod.getStock() + ")");
            }
            
            // Restamos el stock y guardamos el producto
            prod.setStock(prod.getStock() - det.getCantidad());
            productoRepo.save(prod);

            det.setVenta(venta);

            // Cálculos con BigDecimal
            BigDecimal precio = BigDecimal.valueOf(prod.getPrecio());
            BigDecimal cantidad = BigDecimal.valueOf(det.getCantidad());
            BigDecimal sub = precio.multiply(cantidad);

            det.setSubtotal(sub.doubleValue());
            subtotalAcumulado = subtotalAcumulado.add(sub);
        }

        // 🔹 CÁLCULOS DE TOTALES
        BigDecimal igv = subtotalAcumulado.multiply(BigDecimal.valueOf(0.18))
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal total = subtotalAcumulado.add(igv)
                .setScale(2, RoundingMode.HALF_UP);

        venta.setSubtotal(subtotalAcumulado.setScale(2, RoundingMode.HALF_UP).doubleValue());
        venta.setIgv(igv.doubleValue());
        venta.setTotal(total.doubleValue());

        return ventaRepo.save(venta);
    }

    private String generarNumeroComprobante(String tipo) {
        String prefijo = (tipo != null && tipo.equalsIgnoreCase("factura")) ? "F001-" : "B001-";
        long numero = ventaRepo.count() + 1;
        return prefijo + String.format("%06d", numero);
    }
}