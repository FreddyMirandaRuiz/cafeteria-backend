package com.cafeteria.controller;

import com.cafeteria.model.Venta;
import com.cafeteria.service.VentaService;
import com.cafeteria.service.QrService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ventas")
@CrossOrigin(origins = "http://localhost:4200")
public class VentaController {

    private final VentaService ventaService;
    private final QrService qrService;

    public VentaController(VentaService ventaService, QrService qrService) {
        this.ventaService = ventaService;
        this.qrService = qrService;
    }

    @GetMapping
    public ResponseEntity<List<Venta>> listar() {
        return ResponseEntity.ok(ventaService.listar());
    }

    @PostMapping
    public ResponseEntity<?> registrarVenta(@RequestBody Venta venta) {
        try {
            // 1. Intentar registrar la venta (Aquí se valida el stock)
            Venta ventaGuardada = ventaService.registrarVenta(venta);

            // 2. Crear contenido del QR
            String contenidoQr =
                    "Comprobante: " + ventaGuardada.getNumeroComprobante() + "\n" +
                    "Fecha: " + ventaGuardada.getFecha() + "\n" +
                    "Total: S/. " + ventaGuardada.getTotal();

            // 3. Generar QR en base64
            String qrBase64 = qrService.generarQrBase64(contenidoQr);

            // 4. Respuesta exitosa
            return ResponseEntity.ok(new VentaResponse(ventaGuardada, qrBase64));

        } catch (RuntimeException e) {
            // 5. Capturar error de stock o cualquier error de negocio
            Map<String, String> response = new HashMap<>();
            response.put("error", "Error en la operación");
            response.put("message", e.getMessage()); // Aquí va el mensaje "Stock insuficiente para..."
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    record VentaResponse(Venta venta, String qrBase64) {}
}