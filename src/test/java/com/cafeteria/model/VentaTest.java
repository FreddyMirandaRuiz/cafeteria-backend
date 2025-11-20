package com.cafeteria.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class VentaTest {

    @Test
    void testCrearVenta() {
        Venta venta = new Venta();

        venta.setId(1L);
        venta.setCliente("Juan Pérez");
        venta.setTipoComprobante("factura");
        venta.setNumeroComprobante("F001000123");
        venta.setFecha(LocalDateTime.now());
        venta.setSubtotal(50.0);
        venta.setIgv(9.0);
        venta.setTotal(59.0);

        assertEquals(1L, venta.getId());
        assertEquals("Juan Pérez", venta.getCliente());
        assertEquals("factura", venta.getTipoComprobante());
        assertEquals("F001000123", venta.getNumeroComprobante());
        assertNotNull(venta.getFecha());
        assertEquals(50.0, venta.getSubtotal());
        assertEquals(9.0, venta.getIgv());
        assertEquals(59.0, venta.getTotal());
    }

    @Test
    void testAgregarDetallesVenta() {
        Venta venta = new Venta();

        DetalleVenta d1 = new DetalleVenta();
        d1.setCantidad(2);
        d1.setSubtotal(10.0);
        d1.setVenta(venta);

        DetalleVenta d2 = new DetalleVenta();
        d2.setCantidad(1);
        d2.setSubtotal(5.0);
        d2.setVenta(venta);

        venta.setDetalles(Arrays.asList(d1, d2));

        assertEquals(2, venta.getDetalles().size());
        assertEquals(venta, venta.getDetalles().get(0).getVenta());
        assertEquals(10.0, venta.getDetalles().get(0).getSubtotal());
        assertEquals(5.0, venta.getDetalles().get(1).getSubtotal());
    }
    
    @Test
    void testCalculoTotalesManual() {
        Venta venta = new Venta();

        DetalleVenta d1 = new DetalleVenta();
        d1.setSubtotal(10.0);

        DetalleVenta d2 = new DetalleVenta();
        d2.setSubtotal(20.0);

        venta.setDetalles(Arrays.asList(d1, d2));

        double subtotalCalculado = d1.getSubtotal() + d2.getSubtotal();

        // ✅ corregido: redondeo a 2 decimales
        double igvCalculado = Math.round(subtotalCalculado * 0.18 * 100.0) / 100.0;
        double totalCalculado = Math.round((subtotalCalculado + igvCalculado) * 100.0) / 100.0;

        venta.setSubtotal(subtotalCalculado);
        venta.setIgv(igvCalculado);
        venta.setTotal(totalCalculado);

        assertEquals(30.0, venta.getSubtotal());
        assertEquals(5.4, venta.getIgv());
        assertEquals(35.4, venta.getTotal());
    }

    

    @Test
    void testVentaSinDetalles() {
        Venta venta = new Venta();

        venta.setDetalles(null);
        venta.setSubtotal(0.0);
        venta.setIgv(0.0);
        venta.setTotal(0.0);

        assertNull(venta.getDetalles());
        assertEquals(0.0, venta.getSubtotal());
        assertEquals(0.0, venta.getIgv());
        assertEquals(0.0, venta.getTotal());
    }

    @Test
    void testFechaAsignada() {
        Venta venta = new Venta();

        LocalDateTime ahora = LocalDateTime.now();
        venta.setFecha(ahora);

        assertNotNull(venta.getFecha());
        assertEquals(ahora, venta.getFecha());
    }
}
