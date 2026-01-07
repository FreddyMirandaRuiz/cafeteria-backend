package com.cafeteria.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
    	String path = request.getRequestURI();

        // 🔥 RUTAS QUE NO DEBEN PASAR POR JWT
        if (path.startsWith("/api/auth") ||
            path.startsWith("/api/auth-cliente") ||
            path.startsWith("/api/productos") ||
            path.startsWith("/api/resenas") ||
            path.startsWith("/api/ventas") ||
            path.startsWith("/api/pedidos") ||
            path.startsWith("/api/ordenes") ||
            path.startsWith("/api/usuarios") ||
            path.startsWith("/uploads")) {

            filterChain.doFilter(request, response);
            return;
        }

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                // Intenta validar el token
                String email = jwtUtil.obtenerEmail(token);
                request.setAttribute("clienteEmail", email);
            } catch (io.jsonwebtoken.ExpiredJwtException e) {
                // Token expirado
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token expirado");
                return;
            } catch (io.jsonwebtoken.JwtException e) {
                // Token inválido (modificado, corrupto, etc.)
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token inválido");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}