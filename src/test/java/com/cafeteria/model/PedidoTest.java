package com.cafeteria.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class PedidoTest {

    private Pedido pedido;

    @BeforeEach
    void setUp() {
        pedido = new Pedido();
    }

    @Test
    void testConstructorValoresPorDefecto() {
        assertNotNull(pedido.getFecha(), "La fecha no debería ser nula");
        assertEquals("pendiente", pedido.getEstado(), "El estado debería ser 'pendiente'");
    }

    @Test
    void testPrePersistAsignaValoresPorDefecto() {
        pedido.setFecha(null);
        pedido.setEstado(null);
        pedido.prePersist();

        assertNotNull(pedido.getFecha(), "prePersist debería asignar fecha si es nula");
        assertEquals("pendiente", pedido.getEstado(), "prePersist debería asignar estado 'pendiente' si es nulo");
    }

    @Test
    void testAgregarDetalle() {
        DetallePedido detalle = new DetallePedido();
        pedido.agregarDetalle(detalle);

        assertEquals(1, pedido.getDetalles().size(), "Debería tener un detalle agregado");
        assertEquals(pedido, detalle.getPedido(), "El detalle debería apuntar al pedido");
    }

    @Test
    void testSetDetalles() {
        DetallePedido d1 = new DetallePedido();
        DetallePedido d2 = new DetallePedido();

        pedido.setDetalles(Arrays.asList(d1, d2));

        assertEquals(2, pedido.getDetalles().size(), "Debería tener dos detalles");
        assertEquals(pedido, d1.getPedido(), "El primer detalle debería apuntar al pedido");
        assertEquals(pedido, d2.getPedido(), "El segundo detalle debería apuntar al pedido");
    }

    @Test
    void testSetDetallesReemplazaLista() {
        DetallePedido d1 = new DetallePedido();
        pedido.agregarDetalle(d1);

        DetallePedido d2 = new DetallePedido();
        pedido.setDetalles(Arrays.asList(d2));

        assertEquals(1, pedido.getDetalles().size(), "La lista anterior debería reemplazarse");
        assertEquals(d2, pedido.getDetalles().get(0), "La lista debería contener solo el nuevo detalle");
    }
}
