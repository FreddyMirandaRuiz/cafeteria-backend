package com.cafeteria.controller;

import com.cafeteria.model.Pedido;
import com.cafeteria.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "http://localhost:4200") 
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    // ✅ Obtener todos los pedidos
    @GetMapping
    public ResponseEntity<List<Pedido>> listarPedidos() {
        return ResponseEntity.ok(pedidoService.listarPedidos());
    }

    // ✅ Obtener pedido por ID
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> obtenerPedido(@PathVariable Long id) {
        return pedidoService.obtenerPedido(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Crear pedido con detalles
    @PostMapping
    public ResponseEntity<Pedido> crearPedido(@RequestBody Pedido pedido) {
        Pedido nuevo = pedidoService.crearPedido(pedido);
        return ResponseEntity.ok(nuevo);
    }
    
    @PutMapping("/{id}/estado")
    public ResponseEntity<Pedido> actualizarEstado(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String nuevoEstado = body.get("estado");
        Pedido pedidoActualizado = pedidoService.actualizarEstado(id, nuevoEstado);
        return ResponseEntity.ok(pedidoActualizado);
    }
   

    // ✅ Eliminar pedido
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPedido(@PathVariable Long id) {
        pedidoService.eliminarPedido(id);
        return ResponseEntity.noContent().build();
    }
}