package com.cafeteria.controller;

import com.cafeteria.model.Producto;
import com.cafeteria.repository.ProductoRepository;
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

    private final ProductoRepository repo;
    private final String UPLOAD_DIR = "uploads/";

    public ProductoController(ProductoRepository repo) {
        this.repo = repo;
    }

    // 🔹 Listar todos los productos
    @GetMapping
    public List<Producto> listar() {
        return repo.findAll();
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

        if (imagen != null && !imagen.isEmpty()) {
            Files.createDirectories(Paths.get(UPLOAD_DIR));
            String nombreArchivo = System.currentTimeMillis() + "_" + imagen.getOriginalFilename();
            Path rutaArchivo = Paths.get(UPLOAD_DIR + nombreArchivo);
            Files.write(rutaArchivo, imagen.getBytes());
            producto.setImagen(nombreArchivo);
        }

        return repo.save(producto);
    }

    // 🔹 Actualizar producto con imagen opcional
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

        Producto prodExistente = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        prodExistente.setNombre(nombre);
        prodExistente.setPrecio(precio);
        prodExistente.setStock(stock);
        prodExistente.setCategoria(categoria);
        prodExistente.setDescripcion(descripcion != null ? descripcion : prodExistente.getDescripcion());

        if (imagen != null && !imagen.isEmpty()) {
            Files.createDirectories(Paths.get(UPLOAD_DIR));
            String nombreArchivo = System.currentTimeMillis() + "_" + imagen.getOriginalFilename();
            Path rutaArchivo = Paths.get(UPLOAD_DIR + nombreArchivo);
            Files.write(rutaArchivo, imagen.getBytes());
            prodExistente.setImagen(nombreArchivo);
        }

        return repo.save(prodExistente);
    }

    // 🔹 Eliminar producto
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        repo.deleteById(id);
    }
}