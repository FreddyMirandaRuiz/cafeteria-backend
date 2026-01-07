package com.cafeteria.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
//import org.springframework.web.socket.config.annotation.*;
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Endpoint para que Angular se conecte
        registry.addEndpoint("/ws-cafeteria")
                .setAllowedOrigins("http://localhost:4200") // permite conexión desde Angular u otros orígenes// tu frontend Angular
                .withSockJS(); // compatibilidad con navegadores antiguos
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Prefijo de los mensajes salientes
        config.enableSimpleBroker("/topic");
        // Prefijo para los mensajes entrantes
        config.setApplicationDestinationPrefixes("/app");
    }
}