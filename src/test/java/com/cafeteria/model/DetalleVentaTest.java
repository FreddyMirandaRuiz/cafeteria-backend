package com.cafeteria.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DetalleVentaTest {

    @Test
    void testCrearDetalleVenta() {
        DetalleVenta det = new DetalleVenta();

        det.setId(1L);
        det.setCantidad(3);
        det.setSubtotal(15.0);

        assertEquals(1L, det.getId());
        assertEquals(3, det.getCantidad());
        assertEquals(15.0, det.getSubtotal());
    }

    @Test
    void testAsociarProducto() {
        Producto prod = new Producto();
        prod.setId(5L);
        prod.setNombre("Café");
        prod.setPrecio(5.0);

        DetalleVenta det = new DetalleVenta();
        det.setProducto(prod);

        assertNotNull(det.getProducto());
        assertEquals(5L, det.getProducto().getId());
        assertEquals("Café", det.getProducto().getNombre());
        assertEquals(5.0, det.getProducto().getPrecio());
    }

    @Test
    void testAsociarVenta() {
        Venta venta = new Venta();
        venta.setId(10L);

        DetalleVenta det = new DetalleVenta();
        det.setVenta(venta);

        assertNotNull(det.getVenta());
        assertEquals(10L, det.getVenta().getId());
    }

    @Test
    void testCalculoSubtotal() {
        Producto prod = new Producto();
        prod.setPrecio(4.50);

        DetalleVenta det = new DetalleVenta();
        det.setProducto(prod);
        det.setCantidad(2);

        double subtotal = prod.getPrecio() * det.getCantidad();
        det.setSubtotal(subtotal);

        assertEquals(9.0, det.getSubtotal());
    }

    @Test
    void testDetalleVentaSinValoresIniciales() {
        DetalleVenta det = new DetalleVenta();

        assertNull(det.getId());
        assertNull(det.getProducto());
        assertNull(det.getVenta());
        assertNull(det.getCantidad());
        assertNull(det.getSubtotal());
    }
}