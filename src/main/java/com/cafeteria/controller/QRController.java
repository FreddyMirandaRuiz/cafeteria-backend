package com.cafeteria.controller;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@RestController
@RequestMapping("/api/qr")
@CrossOrigin(origins = "*")
public class QRController {

    @GetMapping("/generar")
    public ResponseEntity<String> generarQR(@RequestParam double monto,
                                            @RequestParam(defaultValue = "yape") String metodo,
                                            @RequestParam(defaultValue = "Venta Cafetería") String descripcion)
            throws WriterException, IOException {

        String data = String.format("Método: %s\nMonto: S/ %.2f\nConcepto: %s",
                metodo.toUpperCase(), monto, descripcion);

        // Generar QR con ZXing
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, 250, 250);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        byte[] pngData = pngOutputStream.toByteArray();

        // Convertir a Base64 para mostrar en Angular
        String base64Image = Base64.getEncoder().encodeToString(pngData);
        String dataUri = "data:image/png;base64," + base64Image;

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE)
                .body(dataUri);
    }
}