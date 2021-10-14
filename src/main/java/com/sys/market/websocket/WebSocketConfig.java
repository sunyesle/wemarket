package com.sys.market.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker // stomp webSocket 서버 활성화
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final StompHandler stompHandler;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 해당 경로를 구독하고 있는 클라이언트들에게 메시지 전달
        registry.enableSimpleBroker("/sub");

        //클라이언트에서 보낸 메시지를 받을 prefix
        registry.setApplicationDestinationPrefixes("/pub");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-stomp").setAllowedOriginPatterns("*").withSockJS(); // SocketJS 연결 주소
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        // 헤더의 jwt 토큰을 체크하는 인터셉터 등록
        registration.interceptors(stompHandler);
    }
}
