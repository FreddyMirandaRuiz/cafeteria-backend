package com.cafeteria.controller;

import com.cafeteria.model.Usuario;
import com.cafeteria.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UsuarioControllerTest {

    private UsuarioRepository usuarioRepository;
    private UsuarioController controller;

    @BeforeEach
    void setUp() {
        usuarioRepository = mock(UsuarioRepository.class);
        controller = new UsuarioController(usuarioRepository);
    }

    // ✅ 1. Test listar
    @Test
    void testListarUsuarios() {
        when(usuarioRepository.findAll()).thenReturn(Arrays.asList(new Usuario(), new Usuario()));

        ResponseEntity<?> res = controller.listar();

        assertEquals(200, res.getStatusCodeValue());
        verify(usuarioRepository, times(1)).findAll();
    }

    // ✅ 2. Test registrar
    @Test
    void testRegistrarUsuario() {
        Usuario u = new Usuario();
        u.setNombre("Carlos");

        when(usuarioRepository.save(any(Usuario.class))).thenReturn(u);

        ResponseEntity<?> res = controller.registrar(u);

        assertEquals(200, res.getStatusCodeValue());
        assertEquals("Carlos", ((Usuario) res.getBody()).getNombre());
        verify(usuarioRepository).save(u);
    }

    // ✅ 3. Test eliminar
    @Test
    void testEliminarUsuario() {
        doNothing().when(usuarioRepository).deleteById(1L);

        ResponseEntity<?> res = controller.eliminar(1L);

        assertEquals(200, res.getStatusCodeValue());
        verify(usuarioRepository).deleteById(1L);
    }

    // ✅ 4. Test actualizar usuario correctamente
    @Test
    void testActualizarUsuario() {
        Usuario existente = new Usuario();
        existente.setId(1L);
        existente.setNombre("Antes");

        Usuario datos = new Usuario();
        datos.setNombre("NuevoNombre");
        datos.setUsuario("nuevoUser");
        datos.setPassword("12345");
        datos.setRol(Usuario.Rol.ADMIN);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(existente);

        ResponseEntity<Usuario> res = controller.actualizar(1L, datos);

        Usuario actualizado = res.getBody();

        assertEquals(200, res.getStatusCodeValue());
        assertEquals("NuevoNombre", actualizado.getNombre());
        assertEquals("nuevoUser", actualizado.getUsuario());
        assertEquals("12345", actualizado.getPassword());

        // ✅ Corrección: comparar enum con enum, NO con string
        assertEquals(Usuario.Rol.ADMIN, actualizado.getRol());

        verify(usuarioRepository).save(existente);
    }

    // ✅ 5. Test actualizar usuario — no encontrado
    @Test
    void testActualizarUsuarioNoEncontrado() {
        Usuario datos = new Usuario();
        datos.setNombre("Test");

        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseEntity<Usuario> res = controller.actualizar(99L, datos);

        assertEquals(404, res.getStatusCodeValue());
        verify(usuarioRepository, never()).save(any());
    }
}