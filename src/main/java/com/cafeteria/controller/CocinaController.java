package com.cafeteria.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import com.cafeteria.model.Pedido;
import com.cafeteria.service.PedidoService;

@Controller
public class CocinaController {

    @Autowired
    private PedidoService pedidoService;

    /**
     * 👨‍🍳 El Chef marca que empezó a preparar el pedido.
     * El Service se encarga de actualizar y notificar a todos.
     */
    @MessageMapping("/preparar-pedido")
    public void marcarEnPreparacion(Pedido pedido) {
        System.out.println("🔥 Cocina empezó: Mesa " + pedido.getMesa() + " (ID: " + pedido.getId() + ")");
        // Basta con llamar al service, él ya tiene el messagingTemplate.convertAndSend
        pedidoService.actualizarEstado(pedido.getId(), "en preparación");
    }

    /**
     * ✅ El Chef marca que el pedido está listo.
     */
    @MessageMapping("/pedido-listo")
    public void marcarComoListo(Pedido pedido) {
        System.out.println("🔔 Pedido LISTO: Mesa " + pedido.getMesa());
        pedidoService.actualizarEstado(pedido.getId(), "listo para servir");
    }

    /**
     * 🍽️ El Mozo marca que ya entregó el pedido.
     */
    @MessageMapping("/pedido-servido")
    public void marcarPedidoComoServido(Pedido pedido) {
        System.out.println("🍽️ Pedido entregado: Mesa " + pedido.getMesa());
        pedidoService.actualizarEstado(pedido.getId(), "servido");
    }
}