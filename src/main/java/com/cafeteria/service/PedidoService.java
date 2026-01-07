package com.cafeteria.service;

import com.cafeteria.model.DetallePedido;
import com.cafeteria.model.Pedido;
import com.cafeteria.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // Prefijo unificado para evitar confusiones entre Mozo y Cocina
    private final String TOPIC_PEDIDOS = "/topic/pedidos";

    public List<Pedido> listarPedidos() {
        // Traemos todos los pedidos para que la pantalla inicial esté poblada
        return pedidoRepository.findAll();
    }

    public Optional<Pedido> obtenerPedido(Long id) {
        return pedidoRepository.findById(id);
    }

    @Transactional
    public Pedido crearPedido(Pedido pedido) {
        // 1. Asegurar estado inicial
        pedido.setEstado("pendiente");

        // 2. Vincular detalles y FORZAR el cálculo
        if (pedido.getDetalles() != null) {
            for (DetallePedido detalle : pedido.getDetalles()) {
                detalle.setPedido(pedido);
                // Aseguramos que el detalle tenga su subtotal antes de sumar al total
                detalle.setSubtotal(detalle.getCantidad() * detalle.getPrecio());
            }
            // Llamada explícita para asegurar que 'total' y 'productos' no lleguen en 0 o null
            pedido.actualizarResumenYTotales();
        }

        Pedido nuevoPedido = pedidoRepository.save(pedido);

        // ✅ Notificación a la cocina
        messagingTemplate.convertAndSend(TOPIC_PEDIDOS, nuevoPedido);

        return nuevoPedido;
    }

    @Transactional
    public Pedido actualizarEstado(Long id, String nuevoEstado) {
        Pedido pedidoExistente = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con ID: " + id));

        // Normalizamos a minúsculas para consistencia con el Frontend
        pedidoExistente.setEstado(nuevoEstado.toLowerCase());
        
        Pedido pedidoActualizado = pedidoRepository.save(pedidoExistente);

        // ✅ NOTIFICACIÓN CRÍTICA: 
        // Cuando el Chef presiona "Empezar" o "Listo", este mensaje llega a TODOS.
        // El Mozo verá "En preparación" o "Listo" en su pantalla sin refrescar.
        messagingTemplate.convertAndSend(TOPIC_PEDIDOS, pedidoActualizado);

        return pedidoActualizado;
    }

    @Transactional
    public void eliminarPedido(Long id) {
        // Antes de borrar, notificamos para que desaparezca de las pantallas
        messagingTemplate.convertAndSend(TOPIC_PEDIDOS, "{\"id\":" + id + ", \"deleted\": true}");
        pedidoRepository.deleteById(id);
    }
}