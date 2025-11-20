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
     * 📩 Escucha el mensaje que envía Angular cuando un pedido es marcado como SERVIDO
     * 🔁 Actualiza el estado en la base de datos y lo reenvía al canal /topic/cocina
     */
    @MessageMapping("/pedido-servido")
    public void marcarPedidoComoServido(Pedido pedido) {
        System.out.println("🍽️ Pedido servido recibido desde Angular: " + pedido.getId());
        pedidoService.actualizarEstado(pedido.getId(), "servido");
    }
}
