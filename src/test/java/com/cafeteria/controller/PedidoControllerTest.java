package com.cafeteria.controller;

import com.cafeteria.model.Pedido;
import com.cafeteria.service.PedidoService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class PedidoControllerTest {

    @Mock
    private PedidoService pedidoService;

    @InjectMocks
    private PedidoController pedidoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ✅ Test: listar pedidos
    @Test
    void testListarPedidos() {
        Pedido p1 = new Pedido();
        p1.setId(1L);

        Pedido p2 = new Pedido();
        p2.setId(2L);

        when(pedidoService.listarPedidos()).thenReturn(Arrays.asList(p1, p2));

        ResponseEntity<List<Pedido>> response = pedidoController.listarPedidos();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        verify(pedidoService, times(1)).listarPedidos();
    }

    // ✅ Test: obtener pedido por ID - encontrado
    @Test
    void testObtenerPedidoEncontrado() {
        Pedido pedido = new Pedido();
        pedido.setId(10L);

        when(pedidoService.obtenerPedido(10L)).thenReturn(Optional.of(pedido));

        ResponseEntity<Pedido> response = pedidoController.obtenerPedido(10L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(10L, response.getBody().getId());
        verify(pedidoService, times(1)).obtenerPedido(10L);
    }

    // ✅ Test: obtener pedido por ID - NO encontrado
    @Test
    void testObtenerPedidoNoEncontrado() {
        when(pedidoService.obtenerPedido(20L)).thenReturn(Optional.empty());

        ResponseEntity<Pedido> response = pedidoController.obtenerPedido(20L);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(pedidoService, times(1)).obtenerPedido(20L);
    }

    // ✅ Test: crear pedido
    @Test
    void testCrearPedido() {
        Pedido pedido = new Pedido();
        pedido.setId(1L);

        when(pedidoService.crearPedido(pedido)).thenReturn(pedido);

        ResponseEntity<Pedido> response = pedidoController.crearPedido(pedido);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1L, response.getBody().getId());
        verify(pedidoService, times(1)).crearPedido(pedido);
    }

    // ✅ Test: actualizar estado
    @Test
    void testActualizarEstado() {
        Long id = 5L;
        Map<String, String> body = new HashMap<>();
        body.put("estado", "servido");

        Pedido pedidoActualizado = new Pedido();
        pedidoActualizado.setId(id);
        pedidoActualizado.setEstado("servido");

        when(pedidoService.actualizarEstado(id, "servido")).thenReturn(pedidoActualizado);

        ResponseEntity<Pedido> response = pedidoController.actualizarEstado(id, body);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("servido", response.getBody().getEstado());
        verify(pedidoService, times(1)).actualizarEstado(id, "servido");
    }

    // ✅ Test: eliminar pedido
    @Test
    void testEliminarPedido() {
        Long id = 3L;

        doNothing().when(pedidoService).eliminarPedido(id);

        ResponseEntity<Void> response = pedidoController.eliminarPedido(id);

        assertEquals(204, response.getStatusCodeValue());
        verify(pedidoService, times(1)).eliminarPedido(id);
    }
}
