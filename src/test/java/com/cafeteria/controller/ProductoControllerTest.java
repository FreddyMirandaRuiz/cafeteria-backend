package com.cafeteria.controller;

import com.cafeteria.model.Producto;
import com.cafeteria.repository.ProductoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ProductoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductoRepository repo;

    @InjectMocks
    private ProductoController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    // ✅ Test Listar
    @Test
    void testListarProductos() throws Exception {
        Producto p1 = new Producto();
        p1.setId(1L);
        p1.setNombre("Café");

        Producto p2 = new Producto();
        p2.setId(2L);
        p2.setNombre("Té");

        when(repo.findAll()).thenReturn(Arrays.asList(p1, p2));

        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk());
    }

    // ✅ Test Guardar Producto (con imagen)
    @Test
    void testGuardarProducto() throws Exception {
        MockMultipartFile imagen = new MockMultipartFile(
                "imagen",
                "foto.jpg",
                "image/jpeg",
                "fake image data".getBytes()
        );

        when(repo.save(any(Producto.class))).thenReturn(new Producto());

        mockMvc.perform(multipart("/api/productos")
                        .file(imagen)
                        .param("nombre", "Café")
                        .param("precio", "10.0")
                        .param("stock", "5")
                        .param("categoria", "Bebida")
                        .param("descripcion", "desc"))
                .andExpect(status().isOk());

        verify(repo).save(any(Producto.class));
    }

    // ✅ Test Actualizar Producto (con setters)
    @Test
    void testActualizarProducto() throws Exception {

        Producto existente = new Producto();
        existente.setId(1L);
        existente.setNombre("Café");
        existente.setPrecio(10.0);
        existente.setStock(5);
        existente.setCategoria("Bebida");
        existente.setDescripcion("desc");
        existente.setImagen("img.jpg");

        when(repo.findById(1L)).thenReturn(Optional.of(existente));

        Producto actualizado = new Producto();
        actualizado.setId(1L);
        actualizado.setNombre("Café Actualizado");

        when(repo.save(any(Producto.class))).thenReturn(actualizado);

        MockMultipartFile imagen = new MockMultipartFile(
                "imagen",
                "nueva.jpg",
                "image/jpeg",
                "fake".getBytes()
        );

        mockMvc.perform(
                multipart("/api/productos/1")
                        .file(imagen)
                        .param("nombre", "Café Actualizado")
                        .param("precio", "12.5")
                        .param("stock", "10")
                        .param("categoria", "Bebida")
                        .param("descripcion", "nuevo")
                        .with(request -> { request.setMethod("PUT"); return request; }) // ✅ OBLIGATORIO
        )
        .andExpect(status().isOk());

        verify(repo).save(any(Producto.class));
    }

    // ✅ Test Eliminar
    @Test
    void testEliminarProducto() throws Exception {

        doNothing().when(repo).deleteById(1L);

        mockMvc.perform(delete("/api/productos/1"))
                .andExpect(status().isOk());

        verify(repo).deleteById(1L);
    }
}