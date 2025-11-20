package com.cafeteria.controller;

import com.cafeteria.model.Venta;
import com.cafeteria.service.VentaService;
import com.cafeteria.service.QrService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    // ✅ Listar ventas
    @GetMapping
    public ResponseEntity<List<Venta>> listar() {
        return ResponseEntity.ok(ventaService.listar());
    }

    // ✅ Registrar venta + retornar QR generado
    @PostMapping
    public ResponseEntity<?> registrarVenta(@RequestBody Venta venta) {

        Venta ventaGuardada = ventaService.registrarVenta(venta);

        // ✅ Crear contenido del QR
        String contenidoQr =
                "Comprobante: " + ventaGuardada.getNumeroComprobante() + "\n" +
                "Fecha: " + ventaGuardada.getFecha() + "\n" +
                "Total: S/. " + ventaGuardada.getTotal();

        // ✅ Generar QR en base64
        String qrBase64 = qrService.generarQrBase64(contenidoQr);

        // ✅ Respuesta completa hacia Angular
        return ResponseEntity.ok(new VentaResponse(ventaGuardada, qrBase64));
    }

    // ✅ Clase interna para enviar venta + QR
    record VentaResponse(Venta venta, String qrBase64) {}
}