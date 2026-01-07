package com.cafeteria.controller;

import com.cafeteria.model.Producto;
import com.cafeteria.service.ProductoService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "http://localhost:4200")
public class ProductoController {

    private final ProductoService service; // Inyectamos el servicio, no el repositorio
    private final String UPLOAD_DIR = "uploads/";

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    // 🔹 Listar todos los productos
    @GetMapping
    public List<Producto> listar() {
        return service.listar();
    }

    // 🔹 Obtener un producto por ID
    @GetMapping("/{id}")
    public Producto obtener(@PathVariable Long id) {
        return service.obtener(id);
    }

    // 🔹 Obtener solo el stock de un producto (útil para validación rápida)
    @GetMapping("/{id}/stock")
    public Integer obtenerStock(@PathVariable Long id) {
        Producto p = service.obtener(id);
        return (p != null) ? p.getStock() : 0;
    }

    // 🔹 Guardar producto con imagen opcional
    @PostMapping
    public Producto guardar(
            @RequestParam("nombre") String nombre,
            @RequestParam("precio") Double precio,
            @RequestParam("stock") Integer stock,
            @RequestParam("categoria") String categoria,
            @RequestParam(value = "descripcion", required = false) String descripcion,
            @RequestParam(value = "imagen", required = false) MultipartFile imagen
    ) throws IOException {

        Producto producto = new Producto();
        producto.setNombre(nombre);
        producto.setPrecio(precio);
        producto.setStock(stock);
        producto.setCategoria(categoria);
        producto.setDescripcion(descripcion != null ? descripcion : "");

        // Procesar imagen si existe
        if (imagen != null && !imagen.isEmpty()) {
            String nombreImagen = guardarImagen(imagen);
            producto.setImagen(nombreImagen);
        }

        return service.guardar(producto);
    }

    // 🔹 Actualizar producto existente
    @PutMapping("/{id}")
    public Producto actualizar(
            @PathVariable Long id,
            @RequestParam("nombre") String nombre,
            @RequestParam("precio") Double precio,
            @RequestParam("stock") Integer stock,
            @RequestParam("categoria") String categoria,
            @RequestParam(value = "descripcion", required = false) String descripcion,
            @RequestParam(value = "imagen", required = false) MultipartFile imagen
    ) throws IOException {

        Producto prodExistente = service.obtener(id);
        if (prodExistente == null) {
            throw new RuntimeException("Producto no encontrado con ID: " + id);
        }

        prodExistente.setNombre(nombre);
        prodExistente.setPrecio(precio);
        prodExistente.setStock(stock);
        prodExistente.setCategoria(categoria);
        prodExistente.setDescripcion(descripcion != null ? descripcion : prodExistente.getDescripcion());

        // Actualizar imagen solo si se envía una nueva
        if (imagen != null && !imagen.isEmpty()) {
            String nombreImagen = guardarImagen(imagen);
            prodExistente.setImagen(nombreImagen);
        }

        return service.guardar(prodExistente);
    }

    // 🔹 Eliminar producto
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }

    // 🔹 Método privado para procesar y guardar la imagen en el servidor
    private String guardarImagen(MultipartFile imagen) throws IOException {
        Files.createDirectories(Paths.get(UPLOAD_DIR));
        String nombreArchivo = System.currentTimeMillis() + "_" + imagen.getOriginalFilename();
        Path rutaArchivo = Paths.get(UPLOAD_DIR + nombreArchivo);
        Files.write(rutaArchivo, imagen.getBytes());
        return nombreArchivo;
    }
}