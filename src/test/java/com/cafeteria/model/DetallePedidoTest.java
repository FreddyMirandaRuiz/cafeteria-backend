package com.cafeteria.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DetallePedidoTest {

    @Test
    void testCrearDetallePedido() {
        DetallePedido detalle = new DetallePedido();
        detalle.setNombreProducto("Café");
        detalle.setCantidad(2);
        detalle.setPrecio(5.0);
        detalle.setSubtotal(10.0);

        assertEquals("Café", detalle.getNombreProducto());
        assertEquals(2, detalle.getCantidad());
        assertEquals(5.0, detalle.getPrecio());
        assertEquals(10.0, detalle.getSubtotal());
    }

    @Test
    void testAsignarPedido() {
        Pedido pedido = new Pedido();
        pedido.setMesa("A1");

        DetallePedido detalle = new DetallePedido();
        detalle.setPedido(pedido);

        assertNotNull(detalle.getPedido());
        assertEquals("A1", detalle.getPedido().getMesa());
    }

    @Test
    void testCalculoSubtotal() {
        DetallePedido detalle = new DetallePedido();
        detalle.setCantidad(3);
        detalle.setPrecio(4.5);

        // Calcular subtotal manualmente
        double subtotal = detalle.getCantidad() * detalle.getPrecio();
        detalle.setSubtotal(subtotal);

        assertEquals(13.5, detalle.getSubtotal());
    }

    @Test
    void testDetallePedidoInicializacion() {
        DetallePedido detalle = new DetallePedido();

        assertNull(detalle.getId());
        assertNull(detalle.getNombreProducto());
        assertEquals(0, detalle.getCantidad());
        assertEquals(0.0, detalle.getPrecio());
        assertEquals(0.0, detalle.getSubtotal());
        assertNull(detalle.getPedido());
    }
}
