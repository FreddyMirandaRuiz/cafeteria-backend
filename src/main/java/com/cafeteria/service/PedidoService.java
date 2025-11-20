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

    // ✅ Listar todos los pedidos
    public List<Pedido> listarPedidos() {
        return pedidoRepository.findAll();
    }

    // ✅ Obtener pedido por ID
    public Optional<Pedido> obtenerPedido(Long id) {
        return pedidoRepository.findById(id);
    }

    // ✅ Crear pedido con sus detalles
    public Pedido crearPedido(Pedido pedido) {
        pedido.setEstado("pendiente");

        // 🔗 Vincular los detalles al pedido antes de guardar
        if (pedido.getDetalles() != null && !pedido.getDetalles().isEmpty()) {
            for (DetallePedido detalle : pedido.getDetalles()) {
                detalle.setPedido(pedido);
            }
        }

        Pedido nuevoPedido = pedidoRepository.save(pedido);

        // 🚀 Enviar notificación a cocina (si tienes WebSocket)
        messagingTemplate.convertAndSend("/topic/cocina", nuevoPedido);

        return nuevoPedido;
    }

    // ✅ Actualizar estado del pedido
    @Transactional
    public Pedido actualizarEstado(Long id, String nuevoEstado) {
        int updated = pedidoRepository.actualizarEstado(id, nuevoEstado);
        if (updated == 0) {
            throw new RuntimeException("Pedido no encontrado");
        }

        Pedido pedidoActualizado = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
     // ✅ ACTUALIZAR OBJETO EN MEMORIA PARA QUE EL TEST SEA CORRECTO
        pedidoActualizado.setEstado(nuevoEstado);
        
     // 🚫 Solo enviar al WebSocket si no está entregado
        if (!nuevoEstado.equalsIgnoreCase("entregado") && !nuevoEstado.equalsIgnoreCase("eliminado")) {
            messagingTemplate.convertAndSend("/topic/cocina", pedidoActualizado);
        }

        //messagingTemplate.convertAndSend("/topic/cocina", pedidoActualizado);

        return pedidoActualizado;
    }

    // ✅ Eliminar pedido
    public void eliminarPedido(Long id) {
        pedidoRepository.deleteById(id);
        messagingTemplate.convertAndSend("/topic/cocina", "Pedido eliminado: " + id);
    }
    
 // ✅ Para permitir inyectar mocks en las pruebas unitarias
    public void setPedidoRepository(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public void setMessagingTemplate(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }
}

