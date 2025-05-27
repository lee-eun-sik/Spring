package back.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration

public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 메시지를 받을 endpoint 설정 (브라우저 -> 서버)
        config.enableSimpleBroker("/topic"); // 메시지 브로커가 구독 경로로 사용할 prefix
        config.setApplicationDestinationPrefixes("/app"); // 클라이언트가 서버로 보낼 메시지 경로 prefix
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // SockJS fallback을 사용하는 WebSocket endpoint 등록
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS(); 
    }
}