package com.cafeteria.service.impl;

import com.cafeteria.service.QrService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

@Service
public class QrServiceImpl implements QrService {

    @Override
    public String generarQrBase64(String contenido) {
        try {
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix matrix = writer.encode(contenido, BarcodeFormat.QR_CODE, 250, 250);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(matrix, "PNG", out);

            return "data:image/png;base64," + Base64.getEncoder().encodeToString(out.toByteArray());

        } catch (Exception e) {
            throw new RuntimeException("Error al generar QR: " + e.getMessage());
        }
    }
}
