package com.cafeteria.service;

import com.cafeteria.model.DetallePedido;
import com.cafeteria.model.Pedido;
import com.cafeteria.repository.PedidoRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PedidoServiceTest {

    private PedidoService pedidoService;

    private PedidoRepository pedidoRepository;
    private SimpMessagingTemplate messagingTemplate;

    @BeforeEach
    void setUp() {
        pedidoRepository = mock(PedidoRepository.class);
        messagingTemplate = mock(SimpMessagingTemplate.class);

        pedidoService = new PedidoService();
        pedidoService.setPedidoRepository(pedidoRepository);
        pedidoService.setMessagingTemplate(messagingTemplate);
    }

    // ✅ Test listar pedidos
    @Test
    void testListarPedidos() {
        Pedido p1 = new Pedido();
        Pedido p2 = new Pedido();

        when(pedidoRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        List<Pedido> lista = pedidoService.listarPedidos();

        assertEquals(2, lista.size());
        verify(pedidoRepository, times(1)).findAll();
    }

    // ✅ Test obtener pedido por ID
    @Test
    void testObtenerPedido() {
        Pedido pedido = new Pedido();
        pedido.setId(1L);

        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        Optional<Pedido> resultado = pedidoService.obtenerPedido(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
    }

    // ✅ Test crear pedido con detalles y WebSocket
    @Test
    void testCrearPedido() {
        Pedido pedido = new Pedido();
        DetallePedido d1 = new DetallePedido();
        pedido.setDetalles(Arrays.asList(d1));

        when(pedidoRepository.save(pedido)).thenReturn(pedido);

        Pedido nuevo = pedidoService.crearPedido(pedido);

        assertEquals("pendiente", nuevo.getEstado());
        assertEquals(pedido, d1.getPedido());

        verify(pedidoRepository).save(pedido);
        verify(messagingTemplate)
                .convertAndSend("/topic/cocina", pedido);
    }

    // ✅ Test actualizar estado (CORREGIDO)
    @Test
    void testActualizarEstado() {
        Pedido pedido = new Pedido();
        pedido.setId(1L);
        pedido.setEstado("pendiente");

        when(pedidoRepository.actualizarEstado(1L, "proceso")).thenReturn(1);
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        Pedido actualizado = pedidoService.actualizarEstado(1L, "proceso");

        // ✅ debería venir ya actualizado
        assertEquals("proceso", actualizado.getEstado());

        verify(pedidoRepository).actualizarEstado(1L, "proceso");
        verify(messagingTemplate)
                .convertAndSend("/topic/cocina", pedido);
    }

    // ✅ Test eliminar pedido + WebSocket
    @Test
    void testEliminarPedido() {
        doNothing().when(pedidoRepository).deleteById(10L);

        pedidoService.eliminarPedido(10L);

        verify(pedidoRepository).deleteById(10L);
        verify(messagingTemplate)
                .convertAndSend("/topic/cocina", "Pedido eliminado: 10");
    }
}