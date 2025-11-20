package com.cafeteria.controller;

import static org.mockito.Mockito.verify;

import com.cafeteria.model.Pedido;
import com.cafeteria.service.PedidoService;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CocinaControllerTest {

    @Mock
    private PedidoService pedidoService;

    @InjectMocks
    private CocinaController cocinaController;

    public CocinaControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testMarcarPedidoComoServido() {
        // Crear un pedido de prueba
        Pedido pedido = new Pedido();
        pedido.setId(1L);

        // Llamar al método
        cocinaController.marcarPedidoComoServido(pedido);

        // Verificar que el servicio fue llamado con los parámetros correctos
        verify(pedidoService).actualizarEstado(1L, "servido");
    }
}