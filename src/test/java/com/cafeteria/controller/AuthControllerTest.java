package com.cafeteria.controller;

import com.cafeteria.model.Usuario;
import com.cafeteria.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthControllerTest {

    private UsuarioRepository usuarioRepository;
    private AuthController authController;

    @BeforeEach
    void setUp() {
        usuarioRepository = mock(UsuarioRepository.class);

        // ✅ Inyección manual del mock usando constructor vacío + reflection
        authController = new AuthController();

        try {
            var field = AuthController.class.getDeclaredField("usuarioRepository");
            field.setAccessible(true);
            field.set(authController, usuarioRepository);
        } catch (Exception e) {
            fail("No se pudo inyectar el mock en AuthController");
        }
    }

    // ✅ 1. Login correcto
    @Test
    void testLoginCorrecto() {
        Usuario login = new Usuario();
        login.setUsuario("admin");
        login.setPassword("123");

        Usuario userDB = new Usuario();
        userDB.setId(1L);
        userDB.setUsuario("admin");
        userDB.setPassword("123");

        when(usuarioRepository.findByUsuarioAndPassword("admin", "123"))
                .thenReturn(Optional.of(userDB));

        ResponseEntity<?> response = authController.login(login);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(userDB, response.getBody());
        verify(usuarioRepository, times(1))
                .findByUsuarioAndPassword("admin", "123");
    }

    // ✅ 2. Login incorrecto
    @Test
    void testLoginIncorrecto() {
        Usuario login = new Usuario();
        login.setUsuario("wrongUser");
        login.setPassword("wrongPass");

        when(usuarioRepository.findByUsuarioAndPassword("wrongUser", "wrongPass"))
                .thenReturn(Optional.empty());

        ResponseEntity<?> response = authController.login(login);

        assertEquals(401, response.getStatusCodeValue());
        assertEquals("Credenciales incorrectas", response.getBody());
        verify(usuarioRepository, times(1))
                .findByUsuarioAndPassword("wrongUser", "wrongPass");
    }
}