package com.cafeteria.service;

import com.cafeteria.model.Producto;
import com.cafeteria.repository.ProductoRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductoServiceTest {

    private ProductoService productoService;
    private ProductoRepository repo;

    @BeforeEach
    void setUp() {
        repo = mock(ProductoRepository.class);
        productoService = new ProductoService(repo);
    }

    // ✅ Test listar
    @Test
    void testListar() {
        when(repo.findAll()).thenReturn(Arrays.asList(new Producto(), new Producto()));

        var lista = productoService.listar();

        assertEquals(2, lista.size());
        verify(repo).findAll();
    }

    // ✅ Test guardar
    @Test
    void testGuardar() {
        Producto p = new Producto();
        p.setNombre("Pan");

        when(repo.save(p)).thenReturn(p);

        Producto guardado = productoService.guardar(p);

        assertEquals("Pan", guardado.getNombre());
        verify(repo).save(p);
    }

    // ✅ Test eliminar
    @Test
    void testEliminar() {
        Long id = 10L;

        doNothing().when(repo).deleteById(id);

        productoService.eliminar(id);

        verify(repo).deleteById(id);
    }

    // ✅ Test obtener (existe)
    @Test
    void testObtenerExiste() {
        Producto p = new Producto();
        p.setId(1L);

        when(repo.findById(1L)).thenReturn(Optional.of(p));

        var result = productoService.obtener(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    // ✅ Test obtener (no existe)
    @Test
    void testObtenerNoExiste() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        var result = productoService.obtener(99L);

        assertNull(result);
    }
}
