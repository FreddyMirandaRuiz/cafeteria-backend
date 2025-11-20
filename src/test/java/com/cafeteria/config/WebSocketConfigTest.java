package com.cafeteria.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.StompWebSocketEndpointRegistration;

import static org.mockito.Mockito.*;

public class WebSocketConfigTest {

    private WebSocketConfig webSocketConfig;

    @BeforeEach
    void setUp() {
        webSocketConfig = new WebSocketConfig();
    }

    @Test
    void testRegisterStompEndpoints() {

        // ✅ Mock del registry
        StompEndpointRegistry registry = mock(StompEndpointRegistry.class);

        // ✅ Mock del retorno (chained methods)
        StompWebSocketEndpointRegistration registration = mock(StompWebSocketEndpointRegistration.class);

        when(registry.addEndpoint("/ws-cafeteria")).thenReturn(registration);
        when(registration.setAllowedOriginPatterns()).thenReturn(registration);
        when(registration.withSockJS()).thenReturn(null);

        // ✅ Ejecutar método
        webSocketConfig.registerStompEndpoints(registry);

        // ✅ Verificar que configuró el endpoint
        verify(registry).addEndpoint("/ws-cafeteria");
        verify(registration).setAllowedOriginPatterns();
        verify(registration).withSockJS();
    }

    @Test
    void testConfigureMessageBroker() {

        // ✅ Mock del broker
        MessageBrokerRegistry registry = mock(MessageBrokerRegistry.class);

        // ✅ Ejecutar método
        webSocketConfig.configureMessageBroker(registry);

        // ✅ Validar configuraciones
        verify(registry).enableSimpleBroker("/topic");
        verify(registry).setApplicationDestinationPrefixes("/app");
    }
}
