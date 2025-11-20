package com.cafeteria.repository;

import com.cafeteria.model.Pedido;
import com.cafeteria.model.DetallePedido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PedidoRepositoryTest {

    private PedidoRepository pedidoRepository;

    @BeforeEach
    void setUp() {
        pedidoRepository = mock(PedidoRepository.class);
    }

    // ✅ 1. Probar guardar pedido con detalles y calcular total y cantidad
    @Test
    void testGuardarPedidoConDetalles() {
        Pedido pedido = new Pedido();
        pedido.setMesa("Mesa 1");
        pedido.setMozo("Juan");

        DetallePedido d1 = new DetallePedido();
        d1.setNombreProducto("Café");
        d1.setCantidad(2);
        d1.setPrecio(5.0);
        d1.setSubtotal(10.0);

        DetallePedido d2 = new DetallePedido();
        d2.setNombreProducto("Tarta");
        d2.setCantidad(1);
        d2.setPrecio(7.0);
        d2.setSubtotal(7.0);

        pedido.setDetalles(Arrays.asList(d1, d2));
        pedido.setCantidad(d1.getCantidad() + d2.getCantidad());
        pedido.setTotal(d1.getSubtotal() + d2.getSubtotal());

        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

        Pedido guardado = pedidoRepository.save(pedido);

        assertNotNull(guardado);
        assertEquals(2, guardado.getDetalles().size());
        assertEquals(3, guardado.getCantidad());
        assertEquals(17.0, guardado.getTotal());
        verify(pedidoRepository).save(pedido);
    }

    // ✅ 2. Probar actualización de estado
    @Test
    void testActualizarEstado() {
        Long pedidoId = 1L;
        String nuevoEstado = "entregado";

        when(pedidoRepository.actualizarEstado(pedidoId, nuevoEstado)).thenReturn(1);

        int filasAfectadas = pedidoRepository.actualizarEstado(pedidoId, nuevoEstado);

        assertEquals(1, filasAfectadas);
        verify(pedidoRepository).actualizarEstado(pedidoId, nuevoEstado);
    }

    // ✅ 3. Probar actualización de estado de pedido inexistente
    @Test
    void testActualizarEstadoNoExistente() {
        Long pedidoId = 99L;
        String nuevoEstado = "cancelado";

        when(pedidoRepository.actualizarEstado(pedidoId, nuevoEstado)).thenReturn(0);

        int filasAfectadas = pedidoRepository.actualizarEstado(pedidoId, nuevoEstado);

        assertEquals(0, filasAfectadas);
        verify(pedidoRepository).actualizarEstado(pedidoId, nuevoEstado);
    }

    // ✅ 4. Probar búsqueda de pedido por ID incluyendo detalles
    @Test
    void testBuscarPedidoPorIdConDetalles() {
        Pedido pedido = new Pedido();
        pedido.setId(1L);
        pedido.setMesa("Mesa 2");

        DetallePedido detalle = new DetallePedido();
        detalle.setNombreProducto("Café");
        detalle.setCantidad(1);
        detalle.setPrecio(5.0);
        detalle.setSubtotal(5.0);

        pedido.setDetalles(Arrays.asList(detalle));
        pedido.setCantidad(1);
        pedido.setTotal(5.0);

        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        Optional<Pedido> encontrado = pedidoRepository.findById(1L);

        assertTrue(encontrado.isPresent());
        assertEquals("Mesa 2", encontrado.get().getMesa());
        assertEquals(1, encontrado.get().getDetalles().size());
        assertEquals(5.0, encontrado.get().getTotal());
        assertEquals(1, encontrado.get().getCantidad());
        verify(pedidoRepository).findById(1L);
    }

    // ✅ 5. Probar eliminar pedido
    @Test
    void testEliminarPedido() {
        Long id = 1L;
        doNothing().when(pedidoRepository).deleteById(id);

        pedidoRepository.deleteById(id);

        verify(pedidoRepository).deleteById(id);
    }
}